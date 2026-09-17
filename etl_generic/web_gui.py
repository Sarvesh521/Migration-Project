import os
import json
import threading
from flask import Flask, render_template, jsonify, request

template_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), "templates")
app = Flask(__name__, template_folder=template_dir)

# Global thread state
CURRENT_SCHEMA = {}
APPROVED_SCHEMA = None
APPROVAL_EVENT = threading.Event()


@app.route("/")
def index():
    return render_template("index.html")


@app.route("/api/schema", methods=["GET"])
def get_schema():
    return jsonify(CURRENT_SCHEMA)


@app.route("/api/approve-schema", methods=["POST"])
def approve_schema():
    global APPROVED_SCHEMA
    try:
        data = request.json
        if data:
            APPROVED_SCHEMA = data
            # Save approved schema to output_generic directory
            out_dir = "output_generic"
            os.makedirs(out_dir, exist_ok=True)
            with open(os.path.join(out_dir, "approved_schema.json"), "w", encoding="utf-8") as f:
                json.dump(APPROVED_SCHEMA, f, indent=2)

        APPROVAL_EVENT.set()
        return jsonify({"status": "approved", "message": "Schema approved and saved!"})
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500


def start_gui_server(schema, port=5000, host="0.0.0.0"):
    global CURRENT_SCHEMA, APPROVED_SCHEMA, APPROVAL_EVENT
    CURRENT_SCHEMA = schema
    APPROVED_SCHEMA = None
    APPROVAL_EVENT.clear()

    # Disable Flask banner logging
    import logging
    log = logging.getLogger("werkzeug")
    log.setLevel(logging.ERROR)

    server_thread = threading.Thread(
        target=lambda: app.run(host=host, port=port, debug=False, use_reloader=False),
        daemon=True,
    )
    server_thread.start()
    return APPROVAL_EVENT


def wait_for_user_approval(schema, port=5000):
    global APPROVED_SCHEMA
    event = start_gui_server(schema, port=port)

    print("\n==================================================")
    print(" [!] Web GUI Schema Review Studio Active!         ")
    print(f"     Open http://localhost:{port} in your browser ")
    print("     Inspect, modify schema, and click 'Approve'   ")
    print("==================================================")

    # Block until user clicks Approve in browser
    event.wait()

    print("\n[+] Schema approved by user via Web GUI!")
    return APPROVED_SCHEMA if APPROVED_SCHEMA else schema
