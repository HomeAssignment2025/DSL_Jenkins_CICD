from flask import Flask, jsonify
import docker

app = Flask(__name__)
client = docker.from_env()

@app.route('/api/containers', methods=['GET'])
def list_containers():
    containers = client.containers.list()
    response = []
    for container in containers:
        response.append({
            'id': container.short_id,
            'name': container.name,
            'image': container.image.tags
        })
    return jsonify(response)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
