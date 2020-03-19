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

NOTA: Una regla importante para todos los WAF es que simepre se deben implementar en modo de s{olo monitoreo por un periodo de tiempo . Esto con la finalidad de observar las alertas que se muestran al analizar el trafico web dirigido a tu aplicacione web y asi se pueda deterinar si hay reglas de WAF que puedan interferir con el funcionamiento de la misma. 

		apt-get update
		apt-get upgrade
		apt-get install apache2 -y
		systemctl start apache2
		systemctl enable apache2
	## =========================================INSTALACION DE =========================
		#==================================MOD SECURITY CON APACHE2=====================
		apt-get install libapache2-modsecurity -y 
		a2enmod unique_id #construye magic tokens a partir del hostname
		##(EAI 5)No address associated with hostname: mod_unique_id: unable to find IPv4 address of
		#Si sale ese error deshabilitar mod_unique_id, pero supuestamente este modulo es necesario.
		systemctl restart apache2
		#Para solucionar el problema exisen dos alternativas : 
			#Asignar un nombre de host queresuleva una direccion IP
			#Hacer que el nombre de host resuelva a una direccion IP

		#Mejor dicho, editar /etc/hosts
		#vi /etc/hosts
		#echo "127.0.0.1 localhost mywebserver" >>/etc/hosts

		systemctl restart apache2

	##===========================================CONFIGURACION======================

		#ModSecurity trabaja utilizando reglas para la deteccion y filtrado de diferentes ##tipos de ataques, las cuales se defines bajo el elnguaje propo. Por defecto incluye ##un conjunto de reglas genericas mantenidas por la comunidad OWASP, las cuales son ##liberadas de forma gratuita y protegen cntra atques b{asicos. Pero tamvien existen #reglas comerciales que protegen contra ataques más avanzados como Botnets, Dos y #backdoors entre otros.
		#PAra evitar inconvenientes se copia el arhcivo de ocnfiguracion modsecurity.conf en otro para empezar la configuracion.
		cd /etc/modsecurity
		cp modsecurity.conf-recommended modsecurity.conf
		#Editaremos el archivo
		vi mod-security.conf
		#POR DEFECTO DESHABILITAR ModSecurity completamente ya que será habilitado en VirtualHOsts específicos 
		#SecRuleEngine DetectionOnly
		#SecRuleEngine Off
		#Las reglas genericas que se incluyen con el paquete se encuentranen el directorio 
		cd /usr/share/modsecurity.crs/
		#Dentro del mismo existen subdirectorio que organizan las regals en diferentes tipos.
		#Para habilitar una regla basta con crear un enlace simbolico en activated_riles.

		#hAbiltamos la regla de detección de ataques SQLi para probar modSecurity.
		cp -a activated_rules/ activated_rules-testing
		cd activated_rules-testing/
		ln -s ../base_rules/modsecurity_crs_41_sql_injection_attacks.conf .
		#A pesar d estar configurado, se puede tomar como apagado por que escribimos SecRuleEngine Off. 
		#El siguiente paso consiste en editar el Virtual HOst de testin a fin de habilitar ModSecurity en modo "DetectionOnly" 
		vi /etc/apache2/sites-available/sitioweb.conf

			  # ModSecurity
        		<IfModule security2_module>
					SecRuleEngine DetectionOnly
					Include "/usr/share/modsecurity-crs/*.conf"
					Include "/usr/share/modsecurity-crs/activated_rules-testing/*.conf"
        		</IfModule>

        #Ponerlo en detenccion only significa que los ataque son detectados y resigtrsado en los logs pero no bloqueados o friltrados.
        #POsteriormente se incluye un archivo que posee las directivas de configuraion que controlan al conjunto de reglas genericas de OWASP.
        #FInalmente se incluyen todos los archivos dentro del directorio que contiene las reglas /usr/share/modsecurity.crs/eactivated_rules-testing/ el cual son links simbolicos a reglas habilitadas
        #Finalmente se reinicia apache para que surjan efecto lac onfiguracion.
        systemctl reload apache2

        #=========================	pruebas
        http://sitioweb.com/?var=1' or '1'='1'
       #Podemos checar los logs 
       	tail -f /var/log/apache2/modsec_audit.log

       	#En apache2 existen dos direcotrios paa manejrar los modulos, estos son :
       		- /etc/apache2/mods_available (modulos integrados en con apache2)
       		- /etc/apache2/mods-enabled (activar los modulos correspondoentes)

$       		cd /etc/apache2/mods-enabled
       		ln -s /etc/apache2/mods-available/proxy.load proxy.load

       	#Configuracion de /etc/apache2/mods-enabled/proxy.conf
       		<IfModule mod_proxy.c>
       			#Enable/disable the handling of HTTP/1.1 "Via:" headers
       				#Full add the server version; Block removes all outgoing via:headers
       			#Set to one of: Off|On|Full|Block

       			ProxyVia:On

       			#To enable the cache as well, edit and uncomment the following lines:
       			#no cacheing without CacheRoot
       			CacheRoot "/var/cache/apache2/proxy"
       			CacheGcInterval 4 
       			CacheMaxExpire 24
       			CacheLastModifiedFactor 0.1
       			CacheDefaultExpire 1
       			#Afain, you probably should change this
       			#NoCache a_domain.com another_domain.edu.joes.garage.com
       		</IfModule>
       		#Esto es una configuracin global del proxy, basicamente se conigura lacache dle proxy inverso.



#=========================OWASP======================
Para obtener el set de reglas de OWASP, entramos a la carpeta de modsecurity.
cd /etc/modsecurity
mv modsecurity.conf-recommended  modsecurity.conf

Descargamos el set utilizando git, para esto descargamos git.

sudo apt install git
git clone https://github.com/SpiderLabs/owasp-modsecurity-crs.git

Descomprimimos el paquete decargado e ingresamos a la carpeta y movemos algunos archivos.

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

Probemos que modSecurity está trabajando. Para esto habilitamos una página web con la siguietne configuración.

vi /etc/apache2/sites-available/000-default.conf

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
