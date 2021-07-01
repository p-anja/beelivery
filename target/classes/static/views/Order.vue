<template>
    <div>
        <main-navigation>
            <router-link to="/">Home</router-link>
        </main-navigation>
        <div id="order-main">
            <div id="order-container">
                <div v-if="step==1" id="articles-select">
                    <h2>Choose your articles</h2>
                    <div id="articles-container">
                        <div v-for="article in articles" :key="article.name" class="article">
                            <div :class="article.selected ? 'article-info selected' : 'article-info'">
                                <div @click="article.selected = !article.selected" class="article-header">
                                    <h3>{{article.name}}</h3>
                                    <p>{{article.description}}</p>
                                </div>
                                <div class="price-and-amount">
                                    <b>{{article.price}} &#8364;</b>
                                    <div class="spacer"></div>
                                    <input
                                    v-if="article.selected"
                                    type="number"
                                    min="1"
                                    v-model="article.amount"
                                    @blur="article.amount = article.amount < 1 ? 1 : article.amount">
                                </div>
                            </div>
                            <img src="img/profile_placeholder.png" alt="article pic">
                            <div class="spacer"></div>
                        </div>
                    </div>
                    <div class="spacer"></div>
                    <div id="total-select">
                        <b>Total price: {{totalPrice.toFixed(2)}} &#8364;</b>
                        <div class="spacer"></div>
                        <button :disabled="totalPrice <= 0" @click="++step">Preview</button>
                    </div>
                </div>
                <div v-if="step==2" id="articles-preview">
                    <h2>Your order</h2>
                    <div id="preview-container">
                        <div id="articles-container">
                            <div v-for="article in selectedArticles" :key="article.name" class="article">
                                <div :class="article.selected ? 'article-info selected' : 'article-info'">
                                    <div @click="article.selected = !article.selected" class="article-header">
                                        <h3>{{article.name}}</h3>
                                        <p>{{article.description}}</p>
                                    </div>
                                    <div class="price-and-amount">
                                        <b>{{article.price}} &#8364;</b>
                                        <div class="spacer"></div>
                                        <input
                                        v-if="article.selected"
                                        type="number"
                                        min="1"
                                        v-model="article.amount"
                                        @blur="article.amount = article.amount < 1 ? 1 : article.amount">
                                    </div>
                                </div>
                                <img src="img/profile_placeholder.png" alt="article pic">
                                <div class="spacer"></div>
                            </div>
                        </div>
                        <div id="order-info">
                            <b>Your total: {{totalPrice}} &#8364;</b>
                            <div>
                                <button @click="--step">Back</button>
                                <div class="spacer"></div>
                                <button class="button-primary" :disabled="totalPrice <= 0">Confirm order</button>
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
        step: 1,

        articles: [
            {
                name: 'Pizza 1',
                description: 'Pizza 1 description. Has some tunas and stuff',
                price: 30.4,
                selected: false,
                amount: 1,
            },
            {
                name: 'Pizza 2',
                description: 'Pizza 2 description. Has some tunas and stuff',
                price: 43.4,
                selected: false,
                amount: 1,
            },
            {
                name: 'Pizza 3',
                description: 'Pizza 3 description. Has some tunas and stuff',
                price: 23.0,
                selected: false,
                amount: 1,
            },
            {
                name: 'Pizza 4',
                description: 'Pizza 4 description. Has some tunas and stuff',
                price: 24.0,
                selected: false,
                amount: 1,
            },
        ],
    }),

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
                    res += a.price * a.amount;
                }
            });
            return res;
        },
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
</style>