FROM ipfs/go-ipfs:v0.4.21

# Working directory
WORKDIR /opt

# Exposed ports
EXPOSE 8080
EXPOSE 4001
EXPOSE 5001

# Add entrypoint into container
ADD src/* /opt/
RUN chmod +x /opt/start_ipfs.sh

ENTRYPOINT /opt/start_ipfs.sh

