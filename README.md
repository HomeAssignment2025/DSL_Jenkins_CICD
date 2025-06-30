# 🚀 CI/CD Assignment — Flask API + Nginx + Jenkins + Kubernetes + KEDA

This project implements a full CI/CD pipeline using Jenkins, Docker, and Kubernetes.  
It builds and tests a Flask application that interacts with Docker, wraps it in a reverse proxy via Nginx, and deploys everything into a Kubernetes cluster with KEDA-based autoscaling.

---

## 🧱 Project Structure

ci-cd-assignment/
├── job-dsl/ # Jenkins Job DSL Groovy script
├── flask-api/ # Flask app, Dockerfile, Jenkinsfile
├── nginx-proxy/ # Nginx reverse proxy, Dockerfile, Jenkinsfile
├── runner-job/ # Jenkins pipeline to run and verify locally
├── k8s/ # K8s manifests: deployments, services, KEDA
├── scripts/ # Helper scripts for minikube, curl test
├── terraform/ # (Optional) infra-as-code
├── README.md # This file
└── .gitignore


---

## 📌 Jenkins Jobs (created by DSL)

Run the **seed job** pointing to:  
`job-dsl/jobs.groovy`

This will generate:

- `build-python-api`
- `build-nginx-proxy`
- `run-local-containers`

Each job is parameterized (image name, tag, etc.).

---

## 🐍 Flask API

- Exposes `/api/containers`
- Uses Docker SDK to list running containers
- Built via multi-stage, non-root Dockerfile
- Healthcheck and probes included

---

## 🌐 Nginx Proxy

- Routes `/api/` to the Flask app
- Injects `X-Forwarded-For` and `X-Real-IP` headers
- Minimal Alpine image, non-root, with healthcheck

---

## ☸️ Kubernetes Setup (minikube)

### 1. Start Cluster

scripts/run_minikube.sh
Requires: Docker and minikube installed

2. Create Namespace
kubectl apply -f k8s/namespace.yaml

3. Install KEDA
k8s/keda-install.sh

4. Deploy Application
kubectl apply -f k8s/flask-deployment.yaml
kubectl apply -f k8s/nginx-deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/keda-scaledobject.yaml

5. Port Forward and Test
scripts/port-forward.sh

'Then open in browser or run:'
scripts/test-api.sh

Expected output: list of container IDs running in the cluster (including itself and nginx).

🧪 Local Test via Jenkins
Run job: run-local-containers
This job will:

Start both containers in Docker
Verify connectivity via curl
Clean up everything automatically

🔐 Prerequisites
Jenkins with Docker and Job DSL Plugin
Docker Hub credentials in Jenkins (ID: dockerhub-creds)
docker, kubectl, jq, curl installed

📦 Optional: Terraform
A terraform/ folder is prepared for possible future automation of:
Docker network and container startup
Kubernetes provisioning

✅ Status
This project is self-contained and reproducible.
To test it:

Start from DSL seed job
Trigger pipelines
Deploy and verify in K8s

👤 Author
Illia Rizvash
Email: rizvash.i@gmail.com
LinkedIn: linkedin.com/in/ilya-rizvash