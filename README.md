# Cities app
## How to run
Start mogo instance in any convenient for you way, for example:

`docker run -p 27017:27017 --name mymongo mongo:latest`

or

`brew services start mongodb-community@4.4`


Then start the app:

`mvn spring-boot:run`

Then open http://localhost:8080/ in the browser.

Enjoy!

### Users
App has two default users:

* user/password - readonly user
* user2/password2 - user allowed to edit cities

### Front-end Development mode
To run front-end in development mode do following from the **frontend** folder

`npm start`

To run tests:

`npm test`

To build static resources after changes:

`npm run build-prod`

After that copy **frontend/dist/main.js** into **src/main/resources/public**


## TODO list
* Backend: store users in the the DB instead of default config
* Frontend+backend: enable CSRF and handle it on BE and FE
* Backend: clarify "run it with little-to- zero effort." requirement and make app runnable one command in terminal if needed
* Backend: api doc(swagger?)
* Backend: save updatedBy+timestamp for cities
* Backend: chekstyle and enforcer maven plugins
* Backend: javadocs
* Frontend: proper eslint config
* Frontend: performance optimizations/review unnecessary re-renders
* Frontend: review error in `npm audit`
* Frontend: tests for components separately
* Frontend: improve app UX: styles, handling Enter of filter/ debounce for image url changes
* Frontend+backend: check if user authorized to edit cities and disable Edit button if not
* Frontend: favicon
* Frontend: check browser support
