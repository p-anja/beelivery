<template>
    <div>
        <main-navigation>
        </main-navigation>
        <div id="order-main">
            <div id="order-container">
                <div id="articles-select">
                    <h2>Choose your articles</h2>
                    <div id="articles-container">
                        <div v-for="article in articles" :key="article.name" class="article">
                            <div :class="article.selected ? 'article-info selected' : 'article-info'">
                                <div @click="article.selected = !article.selected" class="article-header">
                                    <h3>
                                        {{article.name}} ({{article.amount}} <span v-if="article.articleType == 'FOOD'">g</span> <span v-else>ml</span>)
                                    </h3>
                                    <p>{{article.description}}</p>
                                </div>
                                <div class="price-and-amount">
                                    <b>
                                        {{article.price}} &#8364;
                                    </b>
                                    <div class="spacer"></div>
                                    <input
                                    v-if="article.selected"
                                    type="number"
                                    min="1"
                                    v-model="article.count"
                                    @blur="article.count = article.count < 1 ? 1 : article.count">
                                </div>
                            </div>
                            <div class="spacer"></div>
                            <img :src="'http://localhost:8080/image/' + article.imageFilepath" alt="article pic">
                        </div>
                    </div>
                    <div class="spacer"></div>
                    <b class="error">{{errors.cart}}</b>
                    <div id="total-select">
                        <b>
                            Total price: {{calculateWithDiscount(totalPrice).toFixed(2)}} &#8364;
                            <span v-if="discountPercent" class="discount">(-{{discountPercent}} %)</span>
                        </b>
                        <div class="spacer"></div>
                        <button class="button-primary" :disabled="totalPrice <= 0" @click="addToCart">Add to cart</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: () => ({
        discountPercent: 0.0,

        errors: {
            cart: '',
        },

        articles: [
            {
                name: 'Pizza 1',
                description: 'Pizza 1 description. Has some tunas and stuff',
                price: 30.4,
                selected: false,
                amount: 1,
                count: 1,
            },
            {
                name: 'Pizza 2',
                description: 'Pizza 2 description. Has some tunas and stuff',
                price: 43.4,
                selected: false,
                amount: 1,
                count: 1,
            },
            {
                name: 'Pizza 3',
                description: 'Pizza 3 description. Has some tunas and stuff',
                price: 23.0,
                selected: false,
                amount: 1,
                count: 1,
            },
            {
                name: 'Pizza 4',
                description: 'Pizza 4 description. Has some tunas and stuff',
                price: 24.0,
                selected: false,
                amount: 1,
                count: 1,
            },
        ],
    }),

    methods: {

        getDiscount: function(){
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            axios.get('/user/discount', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => this.discountPercent = r.data);
        },

        addToCart: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            this.errors.cart = '';
            let data = [];
            this.selectedArticles.forEach(a => {
                data.push({
                    articleName: a.name,
                    amount: a.count,
                });
            });
            axios.post('/user/cart/' + this.$route.params.name, data, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.$router.go(-1))
                .catch(r => this.errors.cart = 'Failed to add');
        },

        getArticles: function() {
            axios.get('/restaurant/' + this.$route.params.name + '/article')
                .then(r => {
                    let articles = r.data;
                    articles.forEach(a => {
                        a.count = 1;
                        a.selected = false;
                    });
                    this.articles = articles;
                });
        },
        calculateWithDiscount: function(price) {
            return price * (100.0 - this.discountPercent) / 100.0;
        },
    },

    computed: {
        selectedArticles: function() {
            let res = [];
            this.articles.forEach(a => {
                if (a.selected) {
                    res.push(a);
                }
            });
            return res;
        },
        totalPrice: function() {
            let res = 0.0;
            this.articles.forEach(a => {
                if(a.selected) {
                    res += a.price * a.count;
                }
            });
            return res;
        },
    },
    mounted() {
        this.getArticles();
        this.getDiscount();
    },
};

</script>

<style scoped>
    #order-main {
        display: grid;
        place-items: center;
        background: #fafafa;
        height: 87vh;
    }

    #order-container {
        display: flex;
        flex-direction: column;
        background: #fff;
        border: solid 1px #eee;
        padding: 10px;
    }

    #articles-select {
        display: flex;
        flex-direction: column;
        height: 60vh;
    }

    #articles-select h2 {
        color: #666;
        margin-bottom: 10px;
    }

    #articles-container {
        height: 400px;
        overflow: auto;
    }

    .article {
        display: flex;
        flex-direction: row;
        padding: 5px;
        /* width: 400px; */
        border-bottom: solid 1px #eee;
    }

    .article-header {
        cursor: pointer;
    }

    .selected {
        background: #eee;
    }
    
    .article img {
        width: 128px;
        height: 128px;
    }

    .article-info {
        width: 320px;
    }

    .article-info h3 {
        font-size: 2.5rem;
    }

    .price-and-amount {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: center;
        padding: 10px;
    }

    .price-and-amount input {
        width: 80px;
    }

    #total-select {
        display: flex;
        flex-direction: row;
    }

    button:disabled {
        background: #eee;
    }

    #preview-container {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 20px;
    }

    #order-info {
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        text-align: right;
    }
    #order-info div {
        display: flex;
        flex-direction: row;
    }

    .discount {
        color: #2ecc71;
    }
</style>