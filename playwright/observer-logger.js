const path = require('path');
const fs = require('fs');

require('dotenv').config({
    path: path.join(__dirname, '.env')
});

const { chromium } = require('@playwright/test');

const BASE_URL =
    'https://admissions.staging.iiitb.net/';

const OUTPUT_FILE =
    path.join(__dirname, 'exploration.jsonl');

const GRAPH_FILE =
    path.join(__dirname, 'navigation-graph.json');


// ============================================================
// INITIALIZATION
// ============================================================

// Start a fresh exploration file for every run.
fs.writeFileSync(OUTPUT_FILE, '');

let interactionCounter = 0;
let requestCounter = 0;
let navigationCounter = 0;

// The most recent meaningful UI interaction.
// Requests occurring after an interaction are associated with it.
let currentInteraction = {
    id: 'I0000',
    type: 'system',
    element: null,
    name: null,
    url: BASE_URL,
    timestamp: new Date().toISOString()
};

// Current frontend page URL.
let currentPageUrl = BASE_URL;

// Current navigation session ID.
let currentNavigationId = 'N0000';

// Timestamp when the current page was entered.
let pageEnteredAt = new Date().toISOString();

// Interactions that occurred on the current page.
let pageInteractions = [];

// API endpoints called from the current page.
let pageApiEndpoints = [];

// Graph data collected across the entire exploration.
// url -> { url, visitCount, interactions, apiEndpoints (Set) }
const graphNodes = new Map();
// Array of { fromUrl, toUrl, trigger }
const graphEdges = [];


// ============================================================
// UTILITY FUNCTIONS
// ============================================================

function writeEvent(event) {
    /*
     * JSONL = one JSON object per line.
     *
     * Example:
     *
     * {"type":"interaction", ...}
     * {"type":"request", ...}
     * {"type":"response", ...}
     */

    fs.appendFileSync(
        OUTPUT_FILE,
        JSON.stringify(event) + '\n'
    );
}


function nextInteractionId() {
    interactionCounter++;

    return `I${String(interactionCounter).padStart(4, '0')}`;
}


function nextRequestId() {
    requestCounter++;

    return `R${String(requestCounter).padStart(5, '0')}`;
}


function nextNavigationId() {
    navigationCounter++;

    return `N${String(navigationCounter).padStart(4, '0')}`;
}


function redactHeaders(headers) {
    const result = {};

    for (const [key, value] of Object.entries(headers)) {

        const lower = key.toLowerCase();

        if (
            lower === 'authorization' ||
            lower === 'cookie' ||
            lower === 'set-cookie' ||
            lower.includes('token') ||
            lower.includes('secret')
        ) {
            result[key] = '<REDACTED>';
        } else {
            result[key] = value;
        }
    }

    return result;
}


function redactSensitiveObject(value) {
    /*
     * Recursively redact obvious credentials/secrets from
     * JSON request/response bodies.
     *
     * This is deliberately conservative: normal application
     * data remains visible for exploration.
     */

    if (Array.isArray(value)) {
        return value.map(item =>
            redactSensitiveObject(item)
        );
    }

    if (
        value !== null &&
        typeof value === 'object'
    ) {
        const result = {};

        for (const [key, val] of Object.entries(value)) {

            const lower = key.toLowerCase();

            if (
                lower === 'password' ||
                lower === 'passwd' ||
                lower === 'token' ||
                lower === 'accesstoken' ||
                lower === 'refreshtoken' ||
                lower === 'secret' ||
                lower === 'authorization'
            ) {
                result[key] = '<REDACTED>';
            } else {
                result[key] =
                    redactSensitiveObject(val);
            }
        }

        return result;
    }

    return value;
}


function parseRequestBody(request) {

    const body = request.postData();

    if (!body) {
        return null;
    }

    /*
     * Most APIs use JSON.
     * If it isn't JSON, retain the raw body.
     */

    try {
        return redactSensitiveObject(
            JSON.parse(body)
        );
    } catch {
        return body;
    }
}


// ============================================================
// NAVIGATION GRAPH FUNCTIONS
// ============================================================

function updateGraphNode(url, interactions, apiEndpoints) {

    if (!graphNodes.has(url)) {
        graphNodes.set(url, {
            url,
            visitCount: 0,
            interactions: [],
            apiEndpoints: new Set()
        });
    }

    const node = graphNodes.get(url);

    node.visitCount++;
    node.interactions.push(...interactions);

    for (const ep of apiEndpoints) {
        node.apiEndpoints.add(
            `${ep.method} ${ep.path}`
        );
    }
}


function emitPageSummary() {

    const now = new Date().toISOString();

    const duration =
        new Date(now) - new Date(pageEnteredAt);

    /*
     * Deduplicate API endpoints with counts.
     */

    const endpointCounts = {};

    for (const ep of pageApiEndpoints) {

        const key =
            `${ep.method} ${ep.path}`;

        if (!endpointCounts[key]) {
            endpointCounts[key] = {
                method: ep.method,
                path: ep.path,
                count: 0
            };
        }

        endpointCounts[key].count++;
    }

    writeEvent({
        type: 'page_summary',

        url: currentPageUrl,

        navigationId:
            currentNavigationId,

        enteredAt: pageEnteredAt,

        leftAt: now,

        durationMs: duration,

        interactionCount:
            pageInteractions.length,

        interactions:
            pageInteractions,

        apiEndpoints:
            Object.values(endpointCounts)
    });

    /*
     * Update graph node data.
     */

    updateGraphNode(
        currentPageUrl,
        pageInteractions,
        pageApiEndpoints
    );
}


function handleNavigation(newUrl, trigger) {

    /*
     * Normalize URLs for comparison.
     * Strip trailing slashes and hashes.
     */

    const normalizedNew =
        newUrl.replace(/\/$/, '');

    const normalizedCurrent =
        currentPageUrl.replace(/\/$/, '');

    if (normalizedNew === normalizedCurrent) {
        return;
    }

    const fromUrl = currentPageUrl;

    /*
     * Emit page summary for the page we are leaving.
     */

    emitPageSummary();

    /*
     * Create navigation event.
     */

    const navId = nextNavigationId();

    writeEvent({
        type: 'navigation',

        navigationId: navId,

        timestamp:
            new Date().toISOString(),

        fromUrl,

        toUrl: newUrl,

        trigger: trigger
            ? {
                interactionId: trigger.id,
                type: trigger.type,
                element: trigger.element,
                name: trigger.name
            }
            : null
    });

    /*
     * Record graph edge.
     */

    graphEdges.push({
        fromUrl,
        toUrl: newUrl,

        trigger: trigger
            ? {
                type: trigger.type,
                element: trigger.element,
                name: trigger.name
            }
            : {
                type: 'navigation',
                element: null,
                name: null
            }
    });

    /*
     * Reset state for the new page.
     */

    currentPageUrl = newUrl;
    currentNavigationId = navId;
    pageEnteredAt = new Date().toISOString();
    pageInteractions = [];
    pageApiEndpoints = [];
}


// ============================================================
// MAIN
// ============================================================

(async () => {

    const browser = await chromium.launch({
        headless: false
    });


    // --------------------------------------------------------
    // BROWSER CONTEXT
    // --------------------------------------------------------

    const context = await browser.newContext({

        /*
         * First authentication layer:
         *
         * HTTP Basic Authentication.
         */
        httpCredentials: {
            username:
                process.env.BASIC_AUTH_USERNAME,

            password:
                process.env.BASIC_AUTH_PASSWORD
        }
    });


    const page = await context.newPage();


    // ========================================================
    // UI INTERACTION CAPTURE
    // ========================================================

    /*
     * Expose a Node.js function to the browser.
     *
     * The browser-side event listener will call this whenever
     * a meaningful UI interaction occurs.
     */

    await page.exposeFunction(
        '__recordInteraction',
        interaction => {

            const interactionId =
                nextInteractionId();


            currentInteraction = {
                id: interactionId,
                type: interaction.type,
                element: interaction.element,
                role: interaction.role,
                name: interaction.name,
                elementId: interaction.elementId,
                nameAttribute:
                    interaction.nameAttribute,
                inputType:
                    interaction.inputType,
                value: interaction.value,
                url: interaction.url,
                timestamp:
                    interaction.timestamp
            };


            /*
             * Write interaction immediately.
             */

            writeEvent({
                type: 'interaction',

                interactionId,

                navigationId:
                    currentNavigationId,

                timestamp:
                    interaction.timestamp,

                interaction: {
                    type: interaction.type,

                    element:
                        interaction.element,

                    role:
                        interaction.role,

                    name:
                        interaction.name,

                    elementId:
                        interaction.elementId,

                    nameAttribute:
                        interaction.nameAttribute,

                    inputType:
                        interaction.inputType,

                    value:
                        interaction.value
                },

                page: {
                    url:
                        interaction.url
                }
            });


            /*
             * Track interaction on the current page.
             */

            pageInteractions.push({
                id: interactionId,
                type: interaction.type,
                element: interaction.element,
                name: interaction.name
            });
        }
    );


    /*
     * Expose a Node.js function to the browser for
     * detecting SPA-style URL changes (pushState,
     * replaceState, popstate, hashchange).
     */

    await page.exposeFunction(
        '__recordNavigation',
        navData => {

            handleNavigation(
                navData.url,
                currentInteraction
            );
        }
    );


    /*
     * Inject listeners into every page loaded by Playwright.
     */

    await page.addInitScript(() => {

        function getActionableElement(element) {

            if (
                !element ||
                !element.tagName
            ) {
                return null;
            }


            /*
             * A click often lands on a child element:
             *
             * <button>
             *     <span>Edit Application</span>
             * </button>
             *
             * event.target may therefore be <span>.
             *
             * Walk upwards until we find the actual actionable
             * element.
             */

            const actionable =
                element.closest(
                    'button, a, input, select, textarea, option, label'
                );

            return actionable || element;
        }


        function getElementDescription(
            element,
            eventType
        ) {

            element =
                getActionableElement(element);


            if (
                !element ||
                !element.tagName
            ) {
                return null;
            }


            const tag =
                element.tagName.toLowerCase();


            /*
             * Prefer human-readable names in this order.
             */

            let name =
                element.getAttribute(
                    'aria-label'
                ) ||

                element.innerText ||

                element.getAttribute(
                    'placeholder'
                ) ||

                element.getAttribute(
                    'title'
                ) ||

                element.value ||

                '';


            name =
                name
                    .replace(/\s+/g, ' ')
                    .trim()
                    .slice(0, 500);


            /*
             * Get explicit ARIA role.
             */

            let role =
                element.getAttribute('role');


            /*
             * Infer common implicit roles.
             */

            if (!role) {

                const roles = {

                    button: 'button',

                    a: 'link',

                    input: {
                        checkbox: 'checkbox',
                        radio: 'radio',
                        button: 'button',
                        submit: 'button',
                        text: 'textbox',
                        email: 'textbox',
                        password: 'textbox'
                    },

                    select: 'combobox',

                    textarea: 'textbox'
                };


                if (
                    tag === 'input'
                ) {
                    role =
                        roles.input[
                            element.type
                        ] || '';
                } else {
                    role =
                        roles[tag] || '';
                }
            }


            return {

                type: eventType,

                element: tag,

                role,

                name,

                elementId:
                    element.id || '',

                nameAttribute:
                    element.getAttribute(
                        'name'
                    ) || '',

                inputType:
                    element.getAttribute(
                        'type'
                    ) || '',

                value:
                    (
                        tag === 'input' ||
                        tag === 'textarea' ||
                        tag === 'select'
                    )
                        ? element.value
                        : '',

                checked:
                    tag === 'input' &&
                    (
                        element.type === 'checkbox' ||
                        element.type === 'radio'
                    )
                        ? element.checked
                        : undefined,

                url:
                    window.location.href,

                timestamp:
                    new Date().toISOString()
            };
        }


        function emit(
            eventType,
            element
        ) {

            const description =
                getElementDescription(
                    element,
                    eventType
                );


            if (!description) {
                return;
            }


            /*
             * The exposed Node function is available after the
             * Playwright page has been initialized.
             */

            if (
                window.__recordInteraction
            ) {
                window.__recordInteraction(
                    description
                );
            }
        }


        // ----------------------------------------------------
        // CLICK
        // ----------------------------------------------------

        document.addEventListener(
            'click',
            event => {

                emit(
                    'click',
                    event.target
                );

            },
            true
        );


        // ----------------------------------------------------
        // CHANGE
        // ----------------------------------------------------

        /*
         * We deliberately use "change" instead of "input".
         *
         * Otherwise typing:
         *
         * J
         * Ji
         * Jin
         * Jine
         * Jines
         *
         * would generate five separate interactions.
         *
         * "change" gives us the completed value instead.
         */

        document.addEventListener(
            'change',
            event => {

                const element =
                    event.target;


                let type =
                    'change';


                if (
                    element.tagName &&
                    element.tagName.toLowerCase()
                        === 'input'
                ) {

                    if (
                        element.type ===
                        'checkbox'
                    ) {

                        type =
                            element.checked
                                ? 'check'
                                : 'uncheck';
                    }


                    else if (
                        element.type ===
                        'radio'
                    ) {

                        type =
                            'select-radio';
                    }
                }


                emit(
                    type,
                    element
                );

            },
            true
        );


        // ----------------------------------------------------
        // FORM SUBMISSION
        // ----------------------------------------------------

        document.addEventListener(
            'submit',
            event => {

                emit(
                    'submit',
                    event.target
                );

            },
            true
        );


        // ----------------------------------------------------
        // SPA NAVIGATION DETECTION
        // ----------------------------------------------------

        /*
         * Single-page applications change the URL without a
         * full page reload using history.pushState or
         * history.replaceState.
         *
         * Monkey-patch these methods and listen for popstate
         * and hashchange to catch all client-side route
         * changes.
         */

        const originalPushState =
            history.pushState;

        const originalReplaceState =
            history.replaceState;


        history.pushState = function(...args) {

            originalPushState.apply(
                this,
                args
            );

            if (window.__recordNavigation) {
                window.__recordNavigation({
                    url:
                        window.location.href,
                    timestamp:
                        new Date().toISOString()
                });
            }
        };


        history.replaceState = function(...args) {

            originalReplaceState.apply(
                this,
                args
            );

            if (window.__recordNavigation) {
                window.__recordNavigation({
                    url:
                        window.location.href,
                    timestamp:
                        new Date().toISOString()
                });
            }
        };


        window.addEventListener(
            'popstate',
            () => {

                if (window.__recordNavigation) {
                    window.__recordNavigation({
                        url:
                            window.location.href,
                        timestamp:
                            new Date().toISOString()
                    });
                }
            }
        );


        window.addEventListener(
            'hashchange',
            () => {

                if (window.__recordNavigation) {
                    window.__recordNavigation({
                        url:
                            window.location.href,
                        timestamp:
                            new Date().toISOString()
                    });
                }
            }
        );

    });


    // ========================================================
    // REQUEST CAPTURE
    // ========================================================

    /*
     * Map Playwright Request objects to our externally
     * generated request IDs.
     */

    const requestMap =
        new Map();


    page.on(
        'request',
        request => {

            const resourceType =
                request.resourceType();


            /*
             * Focus on API-style traffic.
             *
             * xhr/fetch are generally the most useful for
             * understanding application operations.
             */

            if (
                resourceType !== 'xhr' &&
                resourceType !== 'fetch'
            ) {
                return;
            }


            const requestId =
                nextRequestId();


            const url =
                new URL(request.url());


            const timestamp =
                new Date().toISOString();


            const requestData = {

                requestId,

                interactionId:
                    currentInteraction.id,

                interaction:
                    currentInteraction.name,

                timestamp,

                method:
                    request.method(),

                resourceType,

                url: {
                    full:
                        request.url(),

                    protocol:
                        url.protocol,

                    host:
                        url.hostname,

                    port:
                        url.port ||
                        (
                            url.protocol === 'https:'
                                ? 443
                                : 80
                        ),

                    path:
                        url.pathname,

                    query:
                        url.search || null
                },

                headers:
                    redactHeaders(
                        request.headers()
                    ),

                payload:
                    parseRequestBody(
                        request
                    ),

                startTime:
                    Date.now()
            };


            requestMap.set(
                request,
                requestData
            );


            /*
             * Don't write startTime to the final JSON.
             * It's an internal timing value.
             */

            const output = {
                type: 'request',

                requestId,

                interactionId:
                    currentInteraction.id,

                navigationId:
                    currentNavigationId,

                interaction:
                    currentInteraction.name,

                timestamp,

                method:
                    requestData.method,

                resourceType,

                url:
                    requestData.url,

                headers:
                    requestData.headers,

                payload:
                    requestData.payload
            };


            writeEvent(output);


            /*
             * Track API endpoint on the current page.
             */

            pageApiEndpoints.push({
                method: requestData.method,
                path: requestData.url.path
            });

        }
    );


    // ========================================================
    // RESPONSE CAPTURE
    // ========================================================

    page.on(
        'response',
        async response => {

            const request =
                response.request();


            const requestData =
                requestMap.get(
                    request
                );


            /*
             * Ignore responses for which we didn't record the
             * corresponding request.
             */

            if (!requestData) {
                return;
            }


            const endTime =
                Date.now();


            const duration =
                endTime -
                requestData.startTime;


            const headers =
                response.headers();


            const contentType =
                headers['content-type'] ||
                '';


            let body =
                null;


            /*
             * Capture JSON responses.
             */

            if (
                contentType.includes(
                    'application/json'
                )
            ) {

                try {

                    const json =
                        await response.json();


                    body =
                        redactSensitiveObject(
                            json
                        );

                } catch {

                    body =
                        '<JSON body unavailable>';
                }
            }


            const output = {

                type: 'response',

                requestId:
                    requestData.requestId,

                interactionId:
                    requestData.interactionId,

                navigationId:
                    currentNavigationId,

                timestamp:
                    new Date().toISOString(),

                durationMs:
                    duration,

                status:
                    response.status(),

                statusText:
                    response.statusText(),

                url:
                    response.url(),

                contentType,

                headers:
                    redactHeaders(
                        headers
                    ),

                body
            };


            writeEvent(output);

        }
    );


    // ========================================================
    // NAVIGATION
    // ========================================================

    /*
     * Detect hard navigations (full page loads, redirects).
     *
     * SPA-style navigations (pushState, replaceState, popstate,
     * hashchange) are handled by the injected browser-side
     * script above.
     */

    page.on(
        'framenavigated',
        frame => {

            if (frame === page.mainFrame()) {

                handleNavigation(
                    frame.url(),
                    currentInteraction
                );
            }
        }
    );


    await page.goto(
        BASE_URL
    );


    console.log(
        '\n========================================'
    );

    console.log(
        'Playwright exploration started'
    );

    console.log(
        '========================================'
    );

    console.log(
        'Page:',
        page.url()
    );

    console.log(
        'Output:',
        OUTPUT_FILE
    );

    console.log(
        '\nManually explore the application.'
    );

    console.log(
        'Every click/change/submit will be recorded.'
    );

    console.log(
        'API requests and responses will be correlated'
    );

    console.log(
        'using Interaction IDs and Request IDs.'
    );

    console.log(
        '\nPress Resume in Playwright Inspector when done.'
    );


    // ========================================================
    // MANUAL EXPLORATION
    // ========================================================

    await page.pause();


    // ========================================================
    // CLEANUP
    // ========================================================

    /*
     * Emit summary for the final page.
     */

    emitPageSummary();


    /*
     * Build and emit final graph summary.
     */

    const graphOutput = {
        nodes: Array.from(
            graphNodes.values()
        ).map(node => ({
            url: node.url,
            visitCount: node.visitCount,
            interactions: node.interactions,
            apiEndpoints: Array.from(
                node.apiEndpoints
            )
        })),

        edges: graphEdges
    };

    writeEvent({
        type: 'graph_summary',

        timestamp:
            new Date().toISOString(),

        nodes: graphOutput.nodes,

        edges: graphOutput.edges
    });


    /*
     * Write standalone graph file.
     */

    fs.writeFileSync(
        GRAPH_FILE,
        JSON.stringify(
            graphOutput,
            null,
            2
        )
    );


    await browser.close();

    console.log(
        '\nExploration finished.'
    );

    console.log(
        'Graph saved to:',
        GRAPH_FILE
    );

})();