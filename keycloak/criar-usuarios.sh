./kcadm.sh create users -r PolarBookshop \
    -s username=isabelle \
    -s firstName=Isabelle \
    -s lastName=Dahl \
    -s enabled=true

./kcadm.sh add-roles -r PolarBookshop \
    --uusername isabelle \
    --rolename employee \
    --rolename customer


./kcadm.sh create users -r PolarBookshop \
    -s username=bjorn \
    -s firstName=Bjorn \
    -s lastName=Vinterberg\
    -s enabled=true

./kcadm.sh add-roles -r PolarBookshop \
    --uusername bjorn \
    --rolename customer