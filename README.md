


For testing add these enrties

/etc/hosts

127.0.1.1	temp-site-1
127.0.1.1	temp-site-2
127.0.1.1	temp-site-3




Add ` address="127.0.0.1" ` to Connector nodes in $CATALINA_HOMW/conf/server.xml to disable external access
For example:
 -  ` <Connector port="8080" protocol="HTTP/1.1"      address="127.0.0.1"
               connectionTimeout="20000"
               redirectPort="8443"
               maxParameterCount="1000"
               /> `



CSRF tests
 - http://localhost:8080/web-security-holes-01/csrf/fake-page.jsp


