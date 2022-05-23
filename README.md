# Azura-IRC
The IRC Server and Client "used by Azura"!

## Implementation.

### Server
First you need any kind of Server that can run Java 17 and has atleast 2GB of RAM.
After that, you clone the Project and change the Port in the Main class to the wanted port, then you just export it with Maven.


### Client
Secondly, you need a Client Implementation, for this you mostly only have to modify the Wrapper class.
As addition, you have to call the ``Wrapper.getIRCConnector()`` at the start of your Client.
After the user adds credentials, you only have to set them with
``Wrapper.getIRCConnector().username = inputName;`` and ``Wrapper.getIRCConnector().password = inputPassword;``, if you have done all of this, you should be ready to go!

## Note
Please note that you have to add your own Auth-System with an integrated Permission-System to actually and saftly use the IRC.
And please credit us if you use this Code in any way.
