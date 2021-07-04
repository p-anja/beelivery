<template>
    <div>
        <main-navigation></main-navigation>

        <div id="cart-main">
            <div id="cart-container">
                <h1>Your cart</h1>
                <div id="articles">
                    <div v-for="ci in cart.items" :key="ci.article.name" class="article">
                        <div class="article-container">
                            <div class="article-info">
                                <h3>{{ci.article.name}} {{ci.article.amount}} <span v-if="ci.article.articleType == 'FOOD'">g</span><span v-else>ml</span></h3>
                                <p>Amount: {{ci.amount}}</p>
                                <p>{{ci.price}} &euro;</p>
                            </div>
                            <div class="spacer"></div>
                            <div class="article-profile">
                                <img :src="'http://localhost:8080/image/' + ci.article.imageFilepath" alt="article pic">
                            </div>
                        </div>
                        <div class="article-actions">
                            <button @click="deleteArticle(ci)">Remove</button>
                        </div>
                    </div>
                </div>
                <div class="spacer"></div>
                <b class="error">{{errors.order}}</b>
                <div id="cart-actions">
                    <b>Your total: {{cart.price}}</b>
                    <div class="spacer"></div>
                    <button class="button-primary" :disabled="cart.items.length <= 0" @click="createOrder">Order</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: () => ({
        errors: {
            order: '',
        },
        cart: {
            items: [],
            price: 0.0,
        },
    }),

    methods: {
        createOrder: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            this.errors.order = '';

            axios.post('/user/order', {}, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.$router.push('/orders'))
                .catch(r => {
                    console.log(r);
                    this.errors.order = 'Failed to create';
                });
        },

        deleteArticle: function(ci) {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            axios.delete('/user/cart/' + ci.article.name, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => {
                    this.getCart();
                });
        },

        getCart: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            axios.get('/user/cart', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => {
                    this.cart = r.data;
                })
                .catch(r => console.log(r));
        },
    },

    mounted() {
        this.getCart();
    },
};
</script>

<style scoped>
    #cart-main {
        display: grid;
        place-items: center;
        height: 92vh;
        background: #fafafa;
    }

    #cart-container {
        display: flex;
        flex-direction: column;
        padding: 10px;
        background: #fff;
        border: solid 1px #eee;
        min-width: 400px;
    }

    #cart-container h1 {
        color: #666;
        font-size: 4rem;
    }

    #articles {
        padding: 10px;
        height: 400px;
        overflow: auto;
    }

    .article {
        width: 400px;
        display: flex;
        flex-direction: column;
        padding: 5px;
        border-bottom: solid 1px #eee;
    }

    .article-container {
        display: flex;
        flex-direction: row;
    }

    .article-actions {
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
    }

    .article-profile img {
        height: 128px;
        width: 128px;
    }

    .article-info h3 {
        font-size: 2.5rem;
    }

    #cart-actions {
        display: flex;
        flex-direction: row;
        align-items: flex-end;
    }
</style>