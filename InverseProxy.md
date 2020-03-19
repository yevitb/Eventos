El objetivo del desarrollo de una aplicación Web es llegar a infinidad de usuarios, por lo tanto, al ser accesible a un número mucho mayor de usuarios, será más propenso a recibir ataques.

Un WAF es una ayuda para proteger una aplicación.

Un WAF realiza análisis del tráfico web entre el servidor e Internet. Analiza los datos recibidos y protege de diferentes ataques tales como Cross Site Scripting, Remote y Local File Inclussion, SQL Injection, Buffer Overflows, enveneneamiento de Cookies, etc. Uno de los más famosos es mod_security.

Cabe destacar que podemos bloquear peticiones provenientes de un determinado continente, por ejemplo al querer pagar algun servicio en México, ¿por qué tendríamos peticiones de China?

Existen dos formas de implementarlo:

* Deniega todas las transacciones y acepta solo als que cree seguras. PAra ello contiene una seri de reglas predefinidas previamente cargadas mediante un script o aut-aprendidas.

* Acepta todas las peticiones y unicamente bloquea las que son posibles amenazas o ataques reales. 

Un WAF es un intermediario entre usuarios externos y las aplicacioens web. Esto quiere decir que las peticiones y respuestas HTTP son analizadas por el WAF antes de que estas lleguen a las aplicaciones Web o a los usuarios de las aplicaciones.

Para la revision del tráfico HTTP, el WAF aplica un conjunto de reglas para llevar a cabo la deteccion de peticiones HTTP malformadas, ataques como XSS, SQLi, DoS y DDos e incluso de  fuga de infomracion proveniente de la aplicacion web. Cuando el WAF detecta un ataque, intento de intrusion o fuga de información, entonces bloquea el trafico web descartando la petición o respuesta HTTP evitando que los ataques afecten a la aplicacion web o que información sensible sea enviada como respuesta a potenciales usuarios maliciosos.

Modos de implementación: Este depende de la topología de red con la que se cuente y las necesidades dee seguridad requeridas para las aplicaciones web.

* Bridge: Funge como un equipo que interconecta dos segmentos de red de forma transparente (sus interfaces de red no tiene direccion IP), de modo que no se requiere alterar la configuración de direcciones IP de los servidores Web. Permite proteger multiples servidores de aplicacioens web, siempre y cuando estos accedan mediante el canal que protege WAF. 

* Plugin: Se instala como un software de complemento o plugin en el servidor web a proteger. Para su operacion hace uso de recursos de hardware y software del server donde se ha instalado. Su instalación depende totalmente del tipo de servidor web y del sistema operativo subyacente.

* Proxy inverso: Funge ocmo un equipo que interconecta dos o mas segmentos de red, pero este cuenta con direccion IP propia. Concentra, gestiona y analiza las peticiones y respuestas HTTP que circula entre los usuarios y aplicaciones web. En pocas palabras, responde las peticiones web como si este fuera el servidor web mismo, por lo tanto es de utilidad para ocultar a los servidores de alicaciones web de la red exteriror. Permite proteger multiples servidores de aplicaiones web. Su implementacion requiere modificar los registros DNS que ahora deben dirigirse a la direccione IP del WAF en modo proxy inverso en vez de a los servidores web.

NOTA: Una regla importante para todos los WAF es que siempre se deben implementar en modo de sólo monitoreo por un periodo de tiempo . Esto con la finalidad de observar las alertas que se muestran al analizar el trafico web dirigido a tu aplicacione web y asi se pueda deterinar si hay reglas de WAF que puedan interferir con el funcionamiento de la misma. 

Instalación de apache.

		apt-get update
		apt-get upgrade
		apt-get install apache2 -y
		systemctl start apache2
		systemctl enable apache2
		
Instalación de ModSecurity.

		apt-get install libapache2-modsecurity -y 
		a2enmod unique_id #construye magic tokens a partir del hostname
		systemctl restart apache2

ModSecurity trabaja utilizando reglas para la deteccion y filtrado de diferentes tipos de ataques, las cuales se defines bajo el lenguaje propio. Por defecto incluye un conjunto de reglas genéricas mantenidas por la comunidad OWASP, las cuales son liberadas de forma gratuita y protegen contra atques básicos. Pero tambien existen reglas comerciales que protegen contra ataques más avanzados como Botnets, DoS y backdoors, entre otros.

**Configuración reglas OWASP.**

Para obtener el set de reglas de OWASP, entramos a la carpeta de modsecurity.

	cd /etc/modsecurity
	mv modsecurity.conf-recommended  modsecurity.conf
	
Descargamos el set utilizando git, para esto descargamos git.

	sudo apt install git
	git clone https://github.com/SpiderLabs/owasp-modsecurity-crs.git

Descomprimimos el paquete descargado e ingresamos a la carpeta y movemos algunos archivos.

	tar xzf owasp-modsecurity-crs.tgzmv 
	cd owasp-modsecurity-crs
	mv crs-setup.conf.example /etc/modsecurity/crs-setup.conf
	mv rules/ /etc/modsecurity/

Para aplicar las reglas entramos al archivo /etc/apache2/mods-available/security2.conf 

	vi /etc/apache2/mods-available/security2.conf

Incluimos la carpeta rules que acabamos de mover para que se apliquen en nuestro servidor apache.

	<IfModule security2_module>
	        # Default Debian dir for modsecurity's persistent data
	        SecDataDir /var/cache/modsecurity

	        # Include all the *.conf files in /etc/modsecurity.
	        # Keeping your local configuration in that directory
	        # will allow for an easy upgrade of THIS file and
	        # make your life easier
	        IncludeOptional /etc/modsecurity/*.conf
	        Include /etc/modsecurity/rules/*.conf
	</IfModule>

Guardamos y cerramos el archivo. Reiniciamos el servicio de apache

	systemctl restart apache2

Probemos que modSecurity está trabajando. Para esto habilitamos una página web con la siguiente configuración.

	vi /etc/apache2/sites-available/000-default.conf
	
Contenido

 	<VirtualHost *:80>
 	    ServerAdmin webmaster@localhost
 	    DocumentRoot /var/www/html

 	    ErrorLog ${APACHE_LOG_DIR}/error.log
 	    CustomLog ${APACHE_LOG_DIR}/access.log combined

 	    SecRuleEngine On
 	    SecRule ARGS:testparam "@contains test" "id:1234,deny,status:403,msg:'Our test rule has triggered'"
 	</VirtualHost>
	
Guardamos y cerramos. Reiniciamos el servicio de apache

 	systemctl restart apache2

Descargamos curl

	apt-get install curl

Lanzamos una petición

	curl localhost/index.html?exec=/bin/bash

Checamos el archivo de logs de apache, donde podemos apreciar que modSecurity ha detectado un ataque de inyección de comandos.

![Texto alternativo](ModSexurity.png)
