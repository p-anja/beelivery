<template>
    <div>
        <main-navigation>
            <router-link to="/">Home</router-link>
        </main-navigation>
        <div id="rest-main">
            <div id="rest-container">
                <div id="rest-cover">
                    <img src="img/profile_placeholder.png" alt="cover">
                </div>
                <div id="rest-info">
                    <div id="rest-header">
                        <div>
                            <h1>{{restaurant.name}}</h1>
                            <b :class="restaurant.status == 'Open' ? 'open' : 'closed'">{{restaurant.status}}</b>
                            <p>{{restaurant.type}}</p>
                        </div>
                        <div class="spacer"></div>
                        <div id="score-and-buy">
                            <b id="rest-score">{{restaurant.avgScore}}/5.0</b>
                            <router-link :to="'/order/' + restaurant.name" class="button button-primary">Order</router-link>
                        </div>
                    </div>
                    <div id="location">
                        <h2>Where you can find us</h2>
                        <div id="location-container">
                            <div id="location-info">
                                <label>State</label>
                                <p>{{restaurant.address.state}}</p>
                                <label>City</label>
                                <p>{{restaurant.address.city}}</p>
                                <label>Street</label>
                                <p>{{restaurant.address.street}} {{restaurant.address.streetNo}}</p>
                            </div>
                            <div id="map" class="map"></div>
                        </div>
                    </div>

                    <div id="articles-and-comments">
                        <div id="articles">
                            <h2>What we offer</h2>
                            <div id="articles-container">
                                <div class="articles">
                                    <div v-for="article in articles" :key="article.name" class="article">
                                        <div class="article-info">
                                            <h3>{{article.name}}</h3>
                                            <p>{{article.description}}</p>
                                            <b>{{article.price}} &#8364;</b>
                                        </div>
                                        <div class="spacer"></div>
                                        <img src="img/profile_placeholder.png" alt="article pic">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="comments">
                            <h2>Custommer's comments</h2>
                            <div id="comments-container">
                                <div class="comments">
                                    <div v-for="comment in comments" :key="comment.username" class="comment">
                                        <div class="comment-info">
                                            <h3>{{comment.username}}</h3>
                                            <b>{{comment.score}}/5.0</b>
                                            <p>{{comment.body}}</p>
                                        </div>
                                        <div class="spacer"></div>
                                        <img src="img/profile_placeholder.png" alt="commenter pic">
                                    </div>
                                </div>
                                <div id="review-container">
                                    <h3>Share your thoughts</h3>
                                    <label>Rating: </label>
                                    <b>{{review.rating}} / 5</b>
                                    <input type="range" min="1" max="5" v-model="review.rating"/>
                                    <textarea view="review.body" cols="30" rows="10"></textarea>
                                    <button>Add review</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

module.exports = {
    data: () => ({
        allowedToComment: true,
        review: {
            rating: 1,
            body: '',
        },
        comments: [
            {
                username: 'Petar Petrovic',
                score: 4.5,
                body: 'Okej restoran, sta znam. Dobra pica 2',
            },
            {
                username: 'Mirko Mirkovic',
                score: 5.0,
                body: 'Kul',
            },
            {
                username: 'Petar Mirkovic',
                score: 1.5,
                body: 'Fuj!! Hrana grozna1!1 Ne preporucujem uopste, 1.5 samo zbog radnice xexe',
            },
        ],

        articles: [
            {
                name: 'Pizza 1',
                description: 'Pizza 1 description. Has some tunas and stuff',
                price: 30.4,
            },
            {
                name: 'Pizza 2',
                description: 'Pizza 2 description. Has some tunas and stuff',
                price: 43.4,
            },
            {
                name: 'Pizza 3',
                description: 'Pizza 3 description. Has some tunas and stuff',
                price: 23.0,
            },
            {
                name: 'Pizza 4',
                description: 'Pizza 4 description. Has some tunas and stuff',
                price: 24.0,
            },
        ],

        map: null,
        restaurant: {
            name: "Liman",
            status: 'Open',
            type: 'Pizzeria',
            avgScore: 4.3,
            address: {
                state: "Serbia",
                city: "Novi Sad",
                street: "Sekspirova",
                streetNo: "12a",
                lat: 45.2408613,
                lon: 19.8360667,
            },
        },
    }),
    mounted() {
        this.map = new ol.Map({
            target: 'map',
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.OSM()
                })
            ],
            view: new ol.View({
                center: ol.proj.fromLonLat([this.restaurant.address.lon, this.restaurant.address.lat]),
                zoom: 15
            })
        });
        let restPointer = new ol.Feature({
            geometry: new ol.geom.Point(ol.proj.fromLonLat([this.restaurant.address.lon, this.restaurant.address.lat]))
        });
        restPointer.setStyle(
            new ol.style.Style({
                image: new ol.style.Icon({
                    crossOrigin: 'anonymous',
                    src: 'img/map-marker-icon.png',
                    scale: 0.02,
                }),
            })
        );
        var layer = new ol.layer.Vector({
        source: new ol.source.Vector({
            features: [ restPointer,
            ]})});
        this.map.addLayer(layer);
    },
};
</script>

<style scoped>
    #rest-main {
        background: #fafafa;
    }

    #rest-container {
        margin: 0 auto;
        width: 80vw;
    }

    #rest-cover {
        height: 40vh;
    }

    #rest-cover img {
        width: 100%;
        height: 100%;
    }

    #rest-info {
        position: relative;
        padding: 20px;
        width: 90%;
        margin: 0 auto;
        margin-top: -20px;
        z-index: 2;
        background: #fff;
    }

    #location {
        width: 80%;
        margin: 0 auto;
    }

    #location h2 {
        color: #666;
        border-bottom: solid 1px #eee;
    }

    #location-container {
        display: grid;
        grid-template-columns: 1fr auto;
        padding: 20px;
    }

    .map {
        width: 400px;
        height: 400px;
    }

    #rest-header {
        display: flex;
        flex-direction: row;
        width: 60%;
        margin: 0 auto;
        margin-bottom: 20px;
    }

    #rest-header b {
        text-transform: uppercase;
    }

    #score-and-buy {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    #rest-score {
        font-size: 3rem;
        font-weight: 500;
    }

    .open {
        color: #2ecc71;
    }

    .closed {
        color: #e74c3c;
    }

    #articles-and-comments {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 50px;
        width: 80%;
        margin: 0 auto;
    }

    #articles h2 {
        color: #666;
        border-bottom: solid 1px #eee;
    }

    .articles {
        height: 400px;
        /* width: 400px; */
        overflow: auto;
    }

    .article {
        display: flex;
        flex-direction: row;
        padding: 5px;
        /* width: 400px; */
        border-bottom: solid 1px #eee;
    }

    .article img {
        width: 128px;
        height: 128px;
    }

    .article-info h3 {
        font-size: 2.5rem;
    }

    #comments h2 {
        color: #666;
        border-bottom: solid 1px #eee;
    }

    .comments {
        height: 400px;
        overflow: auto;
    }

    .comment {
        display: flex;
        flex-direction: row;
        padding: 5px;
        /* width: 400px; */
        border-bottom: solid 1px #eee;
    }

    .comment img {
        width: 128px;
        height: 128px;
    }

    .comment-info h3 {
        font-size: 2.5rem;
    }

    #review-container {
        display: flex;
        flex-direction: column;
    }

    #review-container h3 {
        margin-top: 20px;
        font-size: 2.5rem;
        color: #666;
    }

</style>