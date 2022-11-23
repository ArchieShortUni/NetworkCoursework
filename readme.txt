Network’s coursework Brief documentation

Included Additional Features:
-	Basic TCP server to connect to
-	GUI
-	Ability to send images (however they must be pngs)

When you start up you have  the choice between connect (which is connecting to a tcp server but also boots up your own separately) or to purely host the tcp server for people to request messages. 

The purely hosting option was mainly for testing, I recommend just using the connect button and connecting to a server
 

Once logged in there is a table of every message currently stored locally
 
By clicking on these rows in the table it will display all the data of that message and if it has an image the image too.

You can run it by via terminal using "java -jar NetworkingCourseworkFin.jar" or by opening it regularly



Warning about image sending:
This is just an after note as I submit I realise that I didnt test the ability to send multiple different images at once. I am not sure if this does work so if 
there is no response to a request with multiple messages with images that might be expected. I realise now that I couldve added an image queue or moved where I looked at the
input to combat this but I ran out of time. 

It was built on java 11
