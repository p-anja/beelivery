<template>
    <div id="home-main">
        <main-navigation></main-navigation>
        <div id="cover-main">
            <div id="moto-container">
                <div id="cover-moto">
                    <h1 id="moto-title">Beelivery</h1>
                    <p id="moto-text">Buzzing to keep you happy</p>
                    <router-link class="button button-primary" to="/signup">Sign up</router-link>
                </div>
            </div>
            <img src="img/bonnie.png" alt="Logo">
        </div>
        <div id="card-home">
            <div id="card-container">
                <div v-for="restaurant in restaurants" :key="restaurant.name" class="card">
                    <img :src="'http://localhost:8080/image/' + restaurant.logoFilepath" alt="restaurant logo">
                    <div class="card-header">
                        <h3>{{restaurant.name}}</h3>
                        <b :class="restaurant.status.toLowerCase()">{{restaurant.status}}</b>
                        <b class="card-type">{{restaurant.restType}}</b>
                        <b>Rating: {{restaurant.avgScore.toFixed(2)}} / 5.0</b>
                    </div>
                    <div class="spacer"></div>
                    <div class="card-actions">
                        <router-link :to="'/restaurant/' + restaurant.name">Go to page ></router-link>
                    </div>
                </div>
            </div>
        </div>
        <div id="find-more">
            <div id="find-more-container">
                <h2>Find more restaurants of your liking</h2>
                <button @click="$router.push('/restsearch')">Search</button>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            restaurants: [],
        }
    },

    methods: {
        getRestaurants: function() {

            let query = '?name=&type=&state=&city=&score=';
            axios.get('/restaurant' + query)
                .then(r => {
                    this.restaurants = r.data;
                    if(this.restaurants) {
                        this.restaurants = this.restaurants.slice(0, 3);
                    }
                })
                .catch(r => console.log(r));
        },
    },

    mounted() {
        this.getRestaurants();
    }
};
</script>

<style scoped>
    #home-main {
        display: flex;
        flex-direction: column;
        background:var(--dark);
        min-height: 100vh;
    }

    #cover-main {
        display: grid;
        grid-template-columns: 1fr 1fr;
        background-color: var(--primary);
        height: 60vh;
        padding: 0 10rem;
    }

    #cover-main img {
        margin-left: auto;
    }

    #moto-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    #cover-moto {
        padding: 10px;
    }

    #card-home {
        display: flex;
        flex-direction: column;
        align-items: center;
        background: #fafafa;
    }

    #card-container {
        margin-top: -2rem;
        --w: 1000px;
        --n: 3;
        width: 60vw;
        display: grid;
        background: none;
        grid-template-columns: repeat(auto-fit, minmax(clamp(100% / (var(--n) + 1) + 0.1%, (var(--w) - 100vw) * 1000, 100%), 1fr));
    }

    .card {
        cursor: pointer;
        display: flex;
        flex-grow: 1;
        flex-shrink: 1;
        flex-basis: 0px;
        flex-direction: column;
        justify-content: flex-start;
        width: 300px;
        border-radius: 15px 15px 15px 15px;
        box-shadow: 0px 0px 30px 0px rgba(0, 42, 106, 0.1);
        /* padding: 2rem; */
        margin: 0rem 1rem;
        background-color: #fff;
        height: 400px;
    }

    .card-header {
        display: flex;
        flex-direction: column;
        text-align: left;
        padding: 10px 10px 0px 10px;
    }

    .card-actions {
        text-align: right;
        padding: 20px;
    }

    .card-actions a {
        color: #222;
        text-decoration: none;
        font-size: 16px;
    }

    .card-actions a:hover {
        color: var(--accent);
        text-decoration: none;
    }

    .card img{
        border-radius: 15px 15px 0px 0px;
        height: 200px;
        width: 300px;
    }

    #find-more {
        display: grid;
        place-items: center;
        min-height: 40vh;
    }

    #find-more-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    #find-more-container h2 {
        padding: 0 0 10px 0;
    }

    #find-more-container button {
        align-self: center;
    }

  .open {
      color: #2ecc71;
  }

  .closed {
      color: #e74c3c;
  }

    .rating {
    --dir: right;
    --fill: gold;
    --fillbg: rgba(100, 100, 100, 0.15);
    --star: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 17.25l-6.188 3.75 1.641-7.031-5.438-4.734 7.172-0.609 2.813-6.609 2.813 6.609 7.172 0.609-5.438 4.734 1.641 7.031z"/></svg>');
    --stars: 5;
    --starsize: 3rem;
    --value: 1;
    --x: calc(100% * (var(--value) / var(--stars)));
    block-size: var(--starsize);
    inline-size: calc(var(--stars) * var(--starsize));
    position: relative;
    touch-action: manipulation;
    -webkit-appearance: none;
    background: #fff;
    }
[dir="rtl"] .rating {
  --dir: left;
}
.rating::-moz-range-track {
  background: linear-gradient(to var(--dir), var(--fill) 0 var(--x), var(--fillbg) 0 var(--x));
  block-size: 100%;
  mask: repeat left center/var(--starsize) var(--star);
}
.rating::-webkit-slider-runnable-track {
  background: linear-gradient(to var(--dir), var(--fill) 0 var(--x), var(--fillbg) 0 var(--x));
  block-size: 100%;
  mask: repeat left center/var(--starsize) var(--star);
  -webkit-mask: repeat left center/var(--starsize) var(--star);
}
.rating::-moz-range-thumb {
  height: var(--starsize);
  opacity: 0;
  width: calc(var(--starsize) / 2);
}
.rating::-webkit-slider-thumb {
  height: var(--starsize);
  opacity: 0;
  width: calc(var(--starsize) / 2);
  -webkit-appearance: none;
}
.rating, .rating-label {
  display: block;
  font-family: ui-sans-serif, system-ui, sans-serif;
}
.rating-label {
  margin-block-end: 1rem;
}
</style>