Ogólne użycie tak jak w oryginalnym GoGui:

Ważniejsze rzeczy:
* GoGui.snapshot();
* GoGui.saveJSON(String fileName)
* vector -> GeoList

Sa chyba wszyskie potrzebne generatory punktow. Chyba wszystkie figury poza kolem mozna uzyskac z WielokontGeneratorStrategy, podajac punkty w roznej kolejnosci mozna zrobic tez figury z przecinajacymi sie liniami.


Update note:
* Dodano obsługę forków - ustawiana w JsonPrinter.

UpdateNote:

* Odkryłem dlaczego czasami nie chcą sie rysować linie, to znaczy odkryłem jak to naprawić a nie dlaczego tak się dzieje :P trzeba w visualization-grunt/public/js/scripts.js zamienić te dwie linijki( TYLKO W SLONKA VERSION):

...

    // draw lines
    for( var i = 0; i < snapshotLines.length; ++i ) {
	
        // stare linijki:
        //var p1=data.getPointByIndex(data.getLineByIndex(snapshotLines[i].lineID).p1 ? data.getLineByIndex(snapshotLines[i].lineID).p1 : data.getLineByIndex(snapshotLines[i].lineID).points[0].pointID);
		//var p2=data.getPointByIndex(data.getLineByIndex(snapshotLines[i].lineID).p2 ? data.getLineByIndex(snapshotLines[i].lineID).p2 : data.getLineByIndex(snapshotLines[i].lineID).points[1].pointID);
		
		//nowe:
		var p1=data.getPointByIndex(data.getLineByIndex(snapshotLines[i].lineID).p1 );
		var p2=data.getPointByIndex(data.getLineByIndex(snapshotLines[i].lineID).p2 );
		
...


Jak będzie czas to dojdę dlaczego tak się dzieje.

IDE:
Po sklonowaniu repo :"mvn clean install idea:idea" lub "mvn clean install eclipse:eclipse", w zaleznosci czego tam uzywacie.

