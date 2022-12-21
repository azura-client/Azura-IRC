# Azura-IRC
This repository contains an IRC-System that can be used for a Minecraft client modification.<br/>
It contains two parts, the first one is the Netty recode developed by [Presti](https://github.com/DxsSucuk).<br/>
The second part is the old Java Socket based System developed by [Azura](https://azura.best).

## [Azura](https://github.com/azura-client/Azura-IRC/tree/main/Socket)

### Server
First you need any kind of server that can run ``Java17`` and has at least 2GB of RAM.<br/>
After that, you clone the project and change the port in the main class to the wanted port, then you just export it with maven.

### Client
Secondly, you need a Client Implementation, for this you mostly only have to modify the Wrapper class.<br/>
In addition, you have to call the ``Wrapper.getIRCConnector()`` at the start of your Client.<br/>
After the user adds credentials, you only have to set them with<br/>
``Wrapper.getIRCConnector().username = inputName;`` and ``Wrapper.getIRCConnector().password = inputPassword;``, if you have done all of this, you should be ready to go!

## [Netty](https://github.com/azura-client/Azura-IRC/tree/main/Netty)
This rewrite is not yet finished, so please don't use it yet.<br/>
The Netty rewrite is based on [netty-chat](https://github.com/marcosQuesada/netty-chat).

## Note
Please note that you have to add your own Auth-System with an integrated Permission-System to actually and safely use the IRC.
And please credit us if you use this code in any way.
