<template>
    <div>
        <main-navigation>
        </main-navigation>
        <div id="rest-main">
            <div id="rest-container">
                <div id="rest-cover">
                    <img :src="'http://localhost:8080/image/' + restaurant.logoFilepath" alt="cover">
                </div>
                <div id="rest-info">
                    <div id="rest-header">
                        <div>
                            <h1>{{restaurant.name}}</h1>
                            <b :class="restaurant.status == 'OPEN' ? 'open' : 'closed'">{{restaurant.status}}</b>
                            <p>{{restaurant.type}}</p>
                        </div>
                        <div class="spacer"></div>
                        <div id="score-and-buy">
                            <b id="rest-score">{{restaurant.avgScore}}/5.0</b>
                            <router-link v-if="isRegular && restaurant.status == 'OPEN'" :to="'/order/' + restaurant.name" class="button button-primary">Order</router-link>
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
                                    <div v-for="article in articles" :key="article.name">
                                        <div v-if="!article.edit" class="article">
                                            <div class="article-info">
                                                <h3>{{article.name}} {{article.amount}}
                                                    <span v-if="article.articleType == 'FOOD' && article.amount">g</span>
                                                    <span v-else-if="article.articleType == 'DRINK' && article.amount">ml</span>
                                                    <span v-if="isManager" @click="editArticle(article, true)" class="edit-article-pencil">
                                                        <i class="fa fa-pencil" aria-hidden="true"></i>
                                                    </span>
                                                </h3>
                                                <b>{{article.articleType}}</b>
                                                <p>{{article.description}}</p>
                                                <b>{{article.price}} &#8364;</b>
                                                <b>{{article.edit}}</b>
                                            </div>
                                            <div class="spacer"></div>
                                            <img :src="'http://localhost:8080/image/' + article.imageFilepath" alt="article pic">
                                        </div>
                                        <div v-else-if="isManager && article.edit" clas="edit-article">
                                            <div class="edit-article-info">
                                                <label>Name</label>
                                                <input type="text" v-model="article.name"
                                                @blur="article.name = !article.name ? 'Enter name' : article.name">
                                                <select v-model="article.articleType">
                                                    <option value="DRINK">Drink</option>
                                                    <option value="FOOD">Food</option>
                                                </select>
                                                <label>Price</label>
                                                <input type="number" v-model="article.price"
                                                @blur="article.price = article.price <= 0 ? article.price = 1 : article.price">
                                                <label>Amount <span v-if="article.articleType == 'FOOD'">g</span><span v-else>ml</span></label>
                                                <input type="number" v-model="article.amount">
                                                <label>Description</label>
                                                <textarea v-model="article.description" cols="30" rows="10"></textarea>
                                            </div>
                                            <div class="edit-article-actions">
                                                <button @click="editArticle(article, false)">Cancel</button>
                                                <div class="spacer"></div>
                                                <button class="button-deny" @click="deleteArticle(article)">Delete</button>
                                                <button class="button-primary" @click="updateArticle(article)">Update</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div v-if="isManager" id="new-article-container">
                                    <div id="new-article-and-profile">
                                        <div id="new-article">
                                            <b class="error">{{errors.articleName}}</b>
                                            <input type="text" placeholder="Article name" v-model="articleName">
                                            <div id="price-and-type">
                                                <input type="number" min="1" v-model="articlePrice"
                                                placeholder="Price"
                                                @blur="articlePrice = articlePrice < 1 ? 1 : articlePrice">
                                                <select v-model="articleType">
                                                    <option value="DRINK">Drink</option>
                                                    <option value="FOOD">Food</option>
                                                </select>
                                            </div>
                                            <input type="number" :placeholder="articleType == 'DRINK' ? 'Amount (ml)' : 'Amount (g)'"
                                                v-model="articleAmount"
                                                min="1"
                                                 @blur="articleAmount = article.Amount < 1 ? 1 : article.Amount">
                                        </div>
                                        <div id="new-article-profile">
                                            <b class="error">{{errors.articleProfile}}</b>
                                            <input type="file" style="display: none" ref="file" @change="selectArticleFile">
                                            <div v-if="!file" @click="$refs.file.click()" id="thumbnail-placholder">
                                                +
                                            </div>
                                            <img :key="imgKey" v-else @click="$refs.file.click()" :src="fileUrl" alt="article profile"/>
                                        </div>
                                    </div>
                                    <textarea placeholder="Description" v-model="articleDescription" cols="30" rows="10"></textarea>
                                    <button class="button-primary" @click="addArticle">Add article</button>
                                </div>
                            </div>
                        </div>
                        <div id="comments">
                            <h2>Custommer's comments</h2>
                            <div id="comments-container">
                                <div class="comments">
                                    <div v-for="comment in comments" :key="comment.username" class="comment">
                                        <div class="comment-container">
                                            <div class="comment-info">
                                                <h3>{{comment.username}}</h3>
                                                <b>{{comment.rating}}/5.0</b>
                                                <b v-if="isManager" :class="comment.status.toLowerCase()">{{comment.status}}</b>
                                                <p>{{comment.body}}</p>
                                            </div>
                                            <div class="spacer"></div>
                                            <img :src="'http://localhost:8080/image/' + comment.userProfile" alt="commenter pic">
                                        </div>
                                        <div v-if="(isManager || isAdmin)" class="comment-actions">
                                            <button class="button-approve" v-if="isManager && comment.status == 'PENDING'" @click="approveComment(comment.id)">Approve</button>
                                            <div class="spacer"></div>
                                            <button class="button-deny" v-if="isManager && comment.status == 'PENDING'" @click="declineComment(comment.id)">Deny</button>
                                            <button class="button-deny" v-if="isAdmin" @click="deleteComment(comment.id)">Delete</button>
                                        </div>
                                    </div>
                                </div>
                                <div v-if="allowedToComment" id="review-container">
                                    <h3>Share your thoughts</h3>
                                    <label>Rating: </label>
                                    <b>{{review.rating}} / 5</b>
                                    <input type="range" min="1" max="5" v-model="review.rating"/>
                                    <textarea view="review.body" cols="30" rows="10" v-model="review.body"></textarea>
                                    <button @click="postComment">Add review</button>
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
        isRegular: false,
        imgKey: 1,
        articleName: '',
        articlePrice: '',
        articleType: 'DRINK',
        articleAmount: '',
        articleDescription: '',
        articleSuccess: false,
        errors: {
            articleName: '',
            articleProfile: '',
            article: '',
        },
        file: '',
        fileUrl: '',
        allowedToComment: false,
        isManager: false,
        isAdmin: false,
        review: {
            rating: 1,
            body: '',
        },
        comments: [
            {
                username: 'Petar Petrovic',
                score: 4.5,
                body: 'Okej restoran, sta znam. Dobra pica 2',
                status: 'Pending',
            },
            {
                username: 'Mirko Mirkovic',
                score: 5.0,
                body: 'Kul',
                status: 'Approved',
            },
            {
                username: 'Petar Mirkovic',
                score: 1.5,
                body: 'Fuj!! Hrana grozna1!1 Ne preporucujem uopste, 1.5 samo zbog radnice xexe',
                status: 'Denied',
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

    methods: {
        approveComment: function(id) {
            if(!localStorage.jws) {
                this.$route.push('/');
                return;
            }

            axios.post('/manager/comment/approve', id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.$router.go(0))
                .catch(r => console.log(r));
        },

        declineComment: function(id) {
            if(!localStorage.jws) {
                this.$route.push('/');
                return;
            }

            axios.post('/manager/comment/decline', id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.$router.go(0))
                .catch(r => console.log(r));
        },

        deleteComment: function(id) {
            if(!localStorage.jws) {
                this.$route.push('/');
                return;
            }

            axios.delete('/admin/comment/' + id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.$router.go(0))
                .catch(r => console.log(r));
        },

        getOwnerComments: function() {
            if(!localStorage.jws) {
                this.$route.push('/');
                return;
            }

            axios.get('/owner/' + this.$route.params.name + '/comment', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => this.comments.push(...r.data))
                .catch(r => console.log(r));
        },
        getComments: function() {
            axios.get('/restaurant/' + this.$route.params.name + '/comment')
                .then(r => this.comments = r.data)
                .catch(r => consoel.log(r));
        },

        postComment: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            axios.post('/user/comment/' + this.$route.params.name, this.review, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.$router.go(0))
                .catch(r => console.log(r));
        },
        
        canComment: function() {
            if(!localStorage.jws) {
                return;
            }

            axios.get('/user/cancomment/' + this.$route.params.name, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => {
                    this.allowedToComment = true;
                })
                .catch(() => {
                    this.allowedToComment = false;
                });
        },

        deleteArticle: function(ar) {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            axios.delete('/manager/restaurant/article/' + ar.name, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => {
                    this.getRestaurant().then(r => {
                        this.restaurant = r.data;
                        if(!this.restaurant.articles) {
                            this.restaurant.articles = [];
                        }
                        this.restaurant.articles.forEach(a => {
                            a.edit = false;
                        })
                    })
                })
                .catch(r => console.log(r));
        },

        updateArticle: function(ar) {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            let data = {
                name: ar.name,
                articleType: ar.articleType,
                amount: ar.amount,
                description: ar.description,
                price: ar.price,
            };

            axios.put('/manager/restaurant/article', data, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => {
                    this.getRestaurant().then(r => {
                        this.restaurant = r.data;
                        if(!this.restaurant.articles) {
                            this.restaurant.articles = [];
                        }
                        this.restaurant.articles.forEach(a => {
                            a.edit = false;
                        })
                    })
                })
                .catch(r => console.log(r));
        },

        editArticle: function(a, val) {
            this.restaurant.articles.forEach(a => a.edit = false);
            a.edit = val;
            this.restaurant.articles = [...this.restaurant.articles];
        },
        validateArticle: function() {
            let nameValid = !!this.articleName;
            let profileValid = !!this.file;

            this.errors.articleName = nameValid ? '' : 'Enter article name';
            this.errors.articleProfile = profileValid ? '' : 'Select image';
            this.errors.article = '';
            this.articleSuccess = false;

            return nameValid && profileValid;
        },

        addArticle: function() {
            if(!this.validateArticle()) {
                return;
            }

            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            let request = {
                name: this.articleName,
                articleType: this.articleType,
                price: this.articlePrice,
                amount: this.articleAmount,
                description: this.articleDescription,
                imageFilepath: '',
            };

            let data = new FormData();
            data.append('file', this.file);
            data.append('request', JSON.stringify(request));

            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': 'Bearer ' + localStorage.jws,
                },
            };
            axios.post('/manager/restaurant/article', data, config)
                .then(() => {
                    this.getRestaurant().then(r => {
                        this.restaurant = r.data;
                        if(!this.restaurant.articles) {
                            this.restaurant.articles = [];
                        }
                        this.restaurant.articles.forEach(a => {
                            a.edit = false;
                        });
                    });
                    this.articleSuccess = true;
                })
                .catch(r => {
                    console.log(r);
                    this.errors.article = 'Failed to add';
                    this.articleSuccess = false;
                });
        },

        selectArticleFile: function() {
            this.file = '';
            if(this.fileUrl) {
                URL.revokeObjectURL(this.fileUrl);
                this.fileUrl = '';
            }
            let file = this.$refs.file.files[0];
            this.fileUrl = URL.createObjectURL(file);
            this.file = file;
            ++this.imgKey;
        },

        getRole: function() {
            if(!localStorage.jws) {
                return;
            }
            axios.get('/user/role', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => {
                    if(r.data == 'ADMIN') {
                        this.isAdmin = true;
                        this.getOwnerComments();
                    }
                    if(r.data == 'MANAGER') {
                        if(!localStorage.jws) {
                            return;
                        }
                        axios.get('/manager/isowner/' + this.$route.params.name, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                            .then(() => {
                                this.isManager = true;
                                this.getOwnerComments();
                            });
                    }
                    if(r.data == 'REGULAR') {
                        this.isRegular = true;
                        this.isManager = false;
                        this.isAdmin = false;
                    }
                })
        },

        getRestaurant: async function() {
            let name = this.$route.params.name;
            return axios.get('/restaurant/' + name);
        },

    },

    mounted() {
        this.getComments();
        this.canComment();
        this.getRole();
        this.getRestaurant()
            .then(r => {
                this.restaurant = r.data;
                if(!this.restaurant.articles) {
                    this.restaurant.articles = [];
                }
                this.restaurant.articles.forEach(a => {
                    a.edit = false;
                });
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
            })
            .catch(r => {
                console.log(r);
                this.$router.push('/');
            });
    },
    computed: {
        articles: function() {
            if(this.restaurant) {
                if(!this.restaurant.articles) {
                    return [];
                }
                return this.restaurant.articles.filter(a => !a.deleted);
            }
            return [];
        }
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

    .comment-container {
        display: flex;
        flex-direction: row;
        /* width: 400px; */
    }

    .comment {
        border-bottom: solid 1px #eee;
        padding: 5px;
    }

    .comment-container img {
        width: 128px;
        height: 128px;
    }

    .comment-info {
        display: flex;
        flex-direction: column;
    }

    .comment-info b {
        text-transform: uppercase;
    }

    .comment-info h3 {
        font-size: 2.5rem;
    }

    .comment-actions {
        display: flex;
        flex-direction: row;
    }

    .approved {
        color: #2ecc71;
    }

    .button-approve {
        color: #fff;
        background-color: #2ecc71;
    }

    .button-deny {
        color: #fff;
        background-color: #e74c3c;
    }

    .pending {
        color: #666;
    }

    .denied {
        color: #e74c3c;
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

    #new-article-container {
        display: flex;
        flex-direction: column;
        padding: 10px;
    }

    #new-article-and-profile {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 10px;
    }

    #new-article {
        display: flex;
        flex-direction: column;
        text-align: left;
    }

    #price-and-type {
        display: flex;
        flex-direction: column;
    }

    #new-article-profile img {
        cursor: pointer;
        width: 128px;
        height: 128px;
    }

    #thumbnail-placholder {
        width: 128px;
        height: 128px;
        cursor: pointer;
        display: grid;
        place-items: center;
        background: #eee;
        font-size: 5rem;
    }    

    .edit-article-pencil {
        color: #666;
        font-size: 2rem;
    }

    .edit-article-actions {
        display: flex;
        flex-direction: row;
        padding: 5px;
    }

</style>