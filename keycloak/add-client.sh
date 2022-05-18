./kcadm.sh config credentials --server http://localhost:8080/auth --realm master \
    --user user --password password


./kcadm.sh create clients -r PolarBookshop \
    -s clientId=edge-service \
    -s enabled=true \
    -s publicClient=false \
    -s secret=polar-keycloak-secret \
    -s 'redirectUris=["http://localhost:9000", "http://localhost:9000/login/oauth2/code/*"]'