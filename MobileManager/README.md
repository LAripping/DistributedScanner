
# MobileManager
An android app to remotely manage the results of nmap_jobs being ran
from the Software Agents, and take actions.
Everything from a mobile dashboard!
The last and coolest installment of the __ds*__ ecosystem.

![screenshot](https://raw.github.com/LAripping/DistributedScanner/master/MobileManager/screenshots/app-drawer.png)


### Quick Start Guide
* The app has no actuall requirements but to be functional,
  the AggregatorManager must be launched in a host visible
  by the Android device, through the network they are connected.
  (that includes _the Internet_)

* Just download the [.apk](https://github.com/LAripping/DistributedScanner/raw/master/MobileManager_v1.0.apk) out of the box, send it to the device, and install the app!

* At launch, set the AggregatorManager's IP and port to retrieve data from.
  This step must only be  executed once.
  ![locate_AM](https://raw.github.com/LAripping/DistributedScanner/master/MobileManager/screenshots/locate.png)

* Then login as a pre-registered user in dsAM's DB, or register a new user. #TODO add screenshot

    * __The latter option is not synchronous__, as the dsAM's admin must actually
      accept the registration, in a window that pop's up in the host running
      the Java application.

    * After successful login, when the app is closed (forced or deliberately)
      the login session is maintained and no re-login is required. __The session
      is destroyed only when the user logs out__ of the application clicking the
      relevant button.

* If the above steps are succesful, the table of registered SoftwareAgents in
  the dsAM node is displayed, each identified by a hash-value, with a dropdown
  listing the available actions. #TODO add screenshot
  _Cool Feature: Pull down to refresh the list!_


### REST API
The mobile client is able to perform almost all the operations
designated for the dsAM module, by retrieving it's data from the
database. Subsequently, an HTTP-based protocol defines the
information to be exchanged, and how to handle it.
In detail: (API visualization coming soon!)

| Method | URL                     | URL Params      | Body                   |  Description                                            |
| :----: | :-------------          | :----------     | :-------               | :------------                                           |
| POST   | <AMurl>`/users/login`   |                 | "username,password"    | Authenticate app user against the database**.           |
| PUT    | <AMurl>`/users`         |                 | "username,password"    | Register new user in the remote database*.              |
| POST   | <AMurl>`/users/logout`  |                 | "username,password"    | User log-out from the app.                              |
| GET    | <AMurl>`/softwareagents`|                 |                        | Get list of software agents registered***.              |
| DELETE | <AMurl>`/softwareagent/`|`?hash=<SA_hash>`|                        | Terminate agent identified by its `hash`.               |
| GET    | <AMurl>`/nmapjobs/`     |`?hash=<SA_hash>`|                        | Get nmap_jobs assigned to agent `SA_hash`               |
| DELETE | <AMurl>`/nmapjobs/<id>/`|                 |                        | `Periodic Job Halt` command, for job `<id>`.            |
| PUT    | <AMurl>`/nmapjobs/`     |`?hash=<SA_hash>`| "parameters,periodic,period" | Assign new job to agent id'ed by `hash`.          |
| GET    | <AMurl>`/results`       |`?hash=<SA_hash>`|                        | Get results for all jobs assigned to agent `hash`***.   |
| GET    | <AMurl>`/results`       |                 |                        | Get all results: Results from all jobs ever assigned***.|

  *Returns `200 OK` in success or `400 Bad Request` otherwise,
  both with an empty response body.

  **Returns `200 OK` in successful login or `406 Not Accepted`
  in authentication failure.

 ***Returns an `ArrayList.toString()` for the requested parameters.

### Data Caching
* On every operation requested, the Android device's network conectivity is checked:
    * If disconnected, the operation is postponed, stored in an _SQLite_ databse/file
      and scheduled for completion when connection with the dsAM is restored.
      The user is notified with the relevant notifications.  A single table is defined
      as the temporary database's schema.

      ```SQLite
        TABLE NAME "nmapjobs"{
            COLUMN NAME "id"        TYPE INTEGER
            COLUMN NAME "params"    TYPE TEXT
            COLUMN NAME "periodic"  TYPE INTEGER
            COLUMN NAME "hash"      TYPE TEXT
        }
        ```


### Credits
The dsMM sub-module was
* Developed in __Android Studio 1.5.1__, for Linux.
* Deployed with __Gradle__.
* Tested to work properly in devices:
     1. __Sony Xperia M__ (C1905) running Android 4.3 "JellyBean".
     2. __LG Google Nexus 5__ running Android 6.0 "Marshmallow"
* The app is compiled with __Android SDK 22__ and generally
  supports devices running at least Android 4.1.2 "Jellybean" (SDK 16).


