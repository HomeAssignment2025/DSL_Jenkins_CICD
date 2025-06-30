# ------------------------------------------------------------------------------
# Custom Docker image for Nginx reverse proxy
# - Uses minimal Alpine-based NGINX image
# - Injects nginx.conf
# - Uses non-root user for security
# - Adds healthcheck for /api route
# ------------------------------------------------------------------------------

    FROM nginx:1.25-alpine

    # Copy custom config
    COPY nginx.conf /etc/nginx/nginx.conf
    
    # Create a non-root user and set proper permissions
    RUN addgroup -S appgroup && adduser -S appuser -G appgroup && \
        chown -R appuser:appgroup /var/cache/nginx /var/run /var/log/nginx /etc/nginx
    
    USER appuser
    
    # Expose proxy port
    EXPOSE 80
    
    # Add simple healthcheck for the /api path
    HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
      CMD wget --spider -q http://localhost/api/ || exit 1
    
    ENTRYPOINT ["nginx", "-g", "daemon off;"]
    
