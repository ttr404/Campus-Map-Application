# Western Campus Map Application
## About The Project
The Campus Map Application is a JavaFX-based desktop software designed to help students, staff, faculty, and visitors navigate the Western University campus. The application provides an interactive map with various Points of Interest (POIs) such as buildings, classrooms, computer labs, washrooms, collaboration rooms, restaurants, etc. Users can log in, search for POIs, and save their favorite locations. The application also provides weather information and accessibility information for different locations on campus.

## Table of Contents
- [Getting Started](#getting-started)
  - [Required Libraries and Third-Party Tools](#required-libraries-and-third-party-tools)
  - [Building from Source Code](#building-from-source-code)
- [Running the Application](#running-the-application)
  - [Main View](#main-view)
  - [Editor Mode](#editor-mode)
  - [Account Information](#account-information)
- [Running the Tests](#running-the-tests)
- [Additional Information](#additional-information)
- [Authors](#authors)

## Getting Started

### Required Libraries and Third-Party Tools
- Java Development Kit (JDK) 19.0.2 or higher
- Maven 3.6.0 or later 
- Jackson Databind 2.14.2 
- Jackson Annotations 2.14.2 
- Jackson Core 2.14.2 
- JavaFX Controls 19.0.2.1 
- JavaFX FXML 19.0.2.1 
- FormsFX Core 11.3.2 
- JSON 20160810 
- JUnit Jupiter API 5.9.2 (for testing)
- JUnit Jupiter 5.9.2 (for testing)
- JetBrains Annotations (for testing)

### Building from Source Code
1.  Install the Java Development Kit (JDK) from the official Oracle website. Follow the instructions for your operating system.
2.  Download and install JavaFX from the official JavaFX website. Follow the instructions for your operating system.
3.  Add JavaFX to your IDE's build path. For example, in IntelliJ IDEA, go to File > Project Structure > Libraries, then click + and add the path to the JavaFX lib folder.
4. Download the Jackson library JAR files from the official Jackson website. Add the following JAR files to your IDE's build path:
   - jackson-core-2.14.2.jar
   - jackson-databind-2.14.2.jar
   - jackson-annotations-2.14.2.jar
5. Download the JUnit Jupiter JAR files from the official JUnit website. Add the following JAR files to your IDE's build path:
   - junit-jupiter-api-5.9.2.jar
6. Clone the repository using the following command:
   `git clone https://repo.csd.uwo.ca/scm/compsci2212_w2023/group17.git`
7. Open the project in your IDE and build the project.
8. The CampusMapApplication class is the main class for the application. You can run the application from this class.

## Running the Application
1. Open the project in your IDE and run the CampusMapApplication class.
2. The application will open in a new window. You can log in using the following credentials provided in the [Account Information](#account-information) section.
3. The current weather information will be displayed bottom of the application.
4. The application will display the main view. Utilize the application as described in the [Main View](#main-view) and [Editor Mode](#editor-mode) sections.

### Main View
1. Use the search bar to search for points of interest(POIs) by room name or room number in a single building.
   - To search for a POI by name, type the name of the POI and click enter.
   - To search for a POI by room number, type the room number and click enter.
   - To view the search results on the map, click on the POI name in the list that appears below the search bar.
2. To view the POI information, click on the POI on the map, a pop-up window will appear with the POI information.
3. To view the floor plan of a building, click on the building name in the drop-down list that appears above the search bar. 
4. To save and remove a POI as a favorite, click on the star icon next to the Editing Mode in the toolbar on top right corner of application.
5. To display and hide layers on map, click on the Layers checkbox in the toolbar.
6. To show all favorite POIs, click on the Favorites button in the toolbar.
7. To Zoom in and out, click on the Zoom In and Zoom Out button in the top toolbar.
8. To reset the map to the default view, click on the Reset button in the top toolbar.
9. To clear the map-marker, click on the Clear button in the top toolbar.
10. To add a user-created POI, click on the map first and then click on the addPOI button in the top toolbar, enter the name and room number of the POI and click on the Add POI button, you will have the option to save the POI as a favorite in the same time.
11. To edit a user-created POI, click on the POI on the map and click on the Edit POI button in the toolbar on the top of the application.
12. To delete a user-created POI, click on the POI on the map and click on the Delete POI button in the toolbar on the top of the application.
13. If the user clicked on the built-in POIs, the add,edit and delete button will be disabled.
14. If the user clicked on the user-created POIs, the add button will be enabled.
15. To sign out and return to log-in view, click on the SignOut button in the top left toolbar, any changes will be automatically saved.
16. To close the application, click on the "x".


### Editor Mode
1. To enable Editor Mode, click on the Editing Mode button in the toolbar on top right corner of application.
2. To add a POI, click on the desired location on the map, a mark will show and filling out name, room number and room type field, then click on the Add POI button in the toolbar on top left corner of application.
3. To edit a POI, click on the POI on the map and click on the Edit POI button in the toolbar on top left corner of application, then filling out name, room number and room type field.
4. To delete a POI, click on the POI on the map and click on the Delete POI button in the toolbar on top left corner of application.
5. After successfully adding, editing, or deleting a POI, the application will automatically save changes to the JSON files. The developer have to restart the application in order for the changes to show-up.
6. To close editing mode and return to the main view, click on the close button on the top left toolbar.

### Account Information
The following accounts are available for testing:
- Username: `admin`, Password: `123`
- Username: `admin2`, Password: `1234`
- Username: `user`, Password: `123`
- Username: `user2`, Password: `1234`
- Username: `user3`, Password: `12345`
- Username: `user4`, Password: `123456`
- Username: `user5`, Password: `1234567`

## Running the Tests
1. Open the project in your IDE and run each test class under test folder as needed.
2. The tests will run and the results will be displayed in the IDE's test window.

## Additional Information
1. Map Data: The Campus Map Application uses a set of map files and POI data stored in the resources folder. To update the maps or POIs, replace the files in this folder with new data following the same format. Make sure to follow the naming conventions and file structure for the application to recognize them correctly. 
2. Accessibility Features: The Campus Map Application provides accessibility information for different locations on campus, such as wheelchair access and accessible entrances. To view this information, click on a POI and check the "Accessibility" section in the POI details. 
3. Offline Mode: The Campus Map Application can work offline, as it does not require an active internet connection to display the maps and POIs. However, weather information may not be available while offline. 
4. Help and Support: If you encounter any issues while using the Campus Map Application or have questions about its functionality, you can consult the user guide included in this README file or contact the development team for further assistance.

## Authors
- Jarrett Boersen(jboerse2@uwo.ca)
- Truman Huang (yhuan939@uwo.ca)
- Yaopeng Xie (yxie447@uwo.ca)
- Binchi Zhang (bzhan484@uwo.ca)
- Tingrui Zhang (tzhan425@uwo.ca)












