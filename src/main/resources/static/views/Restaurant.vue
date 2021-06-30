<template>
    <div>
        <main-navigation>
            <router-link to="/">Home</router-link>
        </main-navigation>
        <div id="rest-main">
            <div id="rest-container">
                <div id="rest-cover">
                    <img src="../assets/logo.png" alt="cover">
                </div>
                <div id="rest-info">
                    <div id="rest-header">
                        <div>
                            <h1>{{restaurant.name}}</h1>
                            <b :class="restaurant.status == 'Open' ? 'open' : 'closed'">{{restaurant.status}}</b>
                            <p>{{restaurant.type}}</p>
                        </div>
                        <div class="spacer"></div>
                        <b id="rest-score">{{restaurant.avgScore}}/5.0</b>
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
                </div>
            </div>
        </div>
    </div>
</template>

<script>

module.exports = {
    data: () => ({
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
                center: ol.proj.fromLonLat([0.0, 0.0]),
                zoom: 4
            })
        });
        console.log(this.map);
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
        margin-top: -20px;
        padding: 20px;
        background: #fff;
    }

    #location {
        width: 80%;
        margin: 0 auto;
    }

    #location h2 {
        border-bottom: solid 1px #eee;
    }

    #location-container {
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

</style>