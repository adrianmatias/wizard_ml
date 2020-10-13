## Given a user profile, get your relevant tarot cards!

## Built with:
 <a href="https://www.playframework.com/">Play<a/>, <a href="https://github.com/swagger-api/swagger-play/tree/master/play-2.6/swagger-play">Swagger<a/>, <a href="http://reactivemongo.org/">ReactiveMongo<a/>, <a href="https://spark.apache.org">Spark<a/> and <a href="https://mleap-docs.combust.ml/">MLeap<a/>

The Wizard uses a recommendation system to model the relevance between Profiles and Card by the rating of the Matches between them.

Currently, the model uses a collaborative filtering approach, implemented with Spark ALS. 
Next steps are considered to apply content based methods to approach the cold start, mainly featurizing Pofiles and Cards.
Alternative approaches could be incorporated to generate a rich model blend.

### Data sources
* Cards: https://www.kaggle.com/lsind18/tarot-json
* Profiles: https://www.kaggle.com/mikahama/celebrities-famous-people-and-their-properties
* Matches: hand curated

### How to run the app

Requirements:
* MongoDB Installed and running on your machine. Tutorial [here](https://docs.mongodb.com/v3.2/tutorial/install-mongodb-on-ubuntu/)
* SBT, maybe available at IDE

Tho ways to run the project:
1. Load data into MongoDB
    - mongoimport --db=wizard --collection=profiles --file=extended_properties_mongoimport.json
    - mongoimport --db=wizard --collection=cards --file=tarot-images_mongoimport.json
    - mongoimport --db=wizard --collection=matchs --file=matches.json
  
2. Import the project
3. Launch Train job
4. cd into the project directory, run SBT and type ```run```  to launch the server or type ```sbt run```.

Then open your favourite browser and go to

```localhost:9000/api-docs```

From the beautiful Swagger-UI interface you can interact with the Wizard to get Tarot cards relevant to given profiles.

### How to run the tests
In the tests directory there are tests written with the [ScalaTest](http://www.scalatest.org/) library.  
To launch them just type ```test``` in a running SBT session or simply type ```sbt test```

Play Template from repository: [play26-swagger-reactivemongo](https://github.com/ricsirigu/play26-swagger-reactivemongo)

Enjoy.

Author: [Adrian Matias](https://github.com/adrianmatias) 