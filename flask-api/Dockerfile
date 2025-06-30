# ------------------------------------------------------------------------------
# Multi-stage Dockerfile for Flask API
# - Stage 1 installs Python dependencies with build tools
# - Stage 2 is a minimal runtime with only what is needed
# - Uses non-root user for security
# ------------------------------------------------------------------------------

# === Stage 1: Builder ===
FROM python:3.11-slim AS builder

WORKDIR /app

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    && pip install --upgrade pip

COPY requirements.txt .
RUN pip install --no-cache-dir --prefix=/install -r requirements.txt

COPY app.py .

# === Stage 2: Runtime Image ===
FROM python:3.11-slim

RUN useradd --create-home appuser

WORKDIR /app

COPY --from=builder /install /usr/local
COPY --from=builder /app /app

RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 5000

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl --fail http://localhost:5000/api/containers || exit 1

ENTRYPOINT ["python", "app.py"]

