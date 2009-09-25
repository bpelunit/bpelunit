VERSIONSNUMMERVERGABE:
- nur die veränderten Plugins erhalten eine neue Versionsnummer
- Versionsnummer des Features wird jedesmal erhöht



ANPASSUNG DER VERSIONSNUMMERN FÜR DIE PLUGINS:
Plugins sind die folgenden Projekte:
- org.bpelunit.framework
- org.bpelunit.client.eclipse
- org.bpelunit.toolsupport

Die Versionsnummer steht in der META-INF/MANIFEST.MF. 
Eclipse sollte einen eigenen Editor für diese Manifest-Datei anzeigen. Im Reiter Overview kann die 
Version geändert werden (Feld "Version"). In einem Texteditor steht die Versionsnummer hinter dem Schlüssel 
"Version-Bundle".

Im org.bpelunit.framework Projekt muss die build.xml noch angepasst werden. Die neue Versionsnummer
muss mit in der Property "plugin.nameAndVersion" stehen.



ANPASSUNG DER VERSIONSNUMMERN FÜR DAS FEATURE:
Das Feature befindet sich im Projekt org.bpelunit.build.eclipse.

Dort muss die feature.xml angepasst werden. Eclipse bietet dafür einen eigenen Editor an. 
Im Reiter Overview muss die neue Feature Version angegeben werden (Feld "Version").
Im Reiter Plugins müssen die Versionsnummern der Plugins angepasst werden (falls verändert).
Plugin aus der linken Liste auswählen und rechts die Versionsnummer angeben.
Bei Bearbeitung mit einem Texteditor, steht die Versionsnummer des Features im Wurzelelement.
Die Versionsnummer der Plugins befinden sich in den Plugin-Elementen.


ANPASSUNG DER VERSIONSNUMMERN FÜR DIE UPDATESITE:
Die updatesite befindet sich im Projekt org.bpelunit.updatesite.

Hier muss die site.xml angepasst werden. Diesmal muss das XML direkt bearbeitet werden 
(Reiter site.xml). Die Attribute url und version im feature-Element müssen entsprechend angepasst werden.


