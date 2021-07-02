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
                <div class="card">
                    <h2 class="card-header">Lorem</h2>
                    <p class="card-text">Lorem ipsum, dolor sit amet consectetur adipisicing elit. Repellendus asperiores maiores nam quia reprehenderit. Illum tempore quod dignissimos rerum laudantium dolorem iure vero est excepturi autem. Voluptatum architecto corporis fugit.</p>
                </div>
                <div class="card">
                    <h2 class="card-header">Lorem</h2>
                    <p class="card-text">Lorem ipsum, dolor sit amet consectetur adipisicing elit. Repellendus asperiores maiores nam quia reprehenderit. Illum tempore quod dignissimos rerum laudantium dolorem iure vero est excepturi autem. Voluptatum architecto corporis fugit.</p>
                </div>
                <div class="card">
                    <h2 class="card-header">Lorem</h2>
                    <p class="card-text">Lorem ipsum, dolor sit amet consectetur adipisicing elit. Repellendus asperiores maiores nam quia reprehenderit. Illum tempore quod dignissimos rerum laudantium dolorem iure vero est excepturi autem. Voluptatum architecto corporis fugit.</p>
                </div>
            </div>
        </div>
        <div id="find-more">
            <div id="find-more-container">
                <h2>Find more restaurants of your liking</h2>
                <button>
                    Search
                </button>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
        }
    },

    methods: {
    },

    mounted() {
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
        display: flex;
        flex-grow: 1;
        flex-shrink: 1;
        flex-basis: 0px;
        flex-direction: column;
        justify-content: space-around;
        max-width: 20vw;
        border-radius: 15px 15px 15px 15px;
        box-shadow: 0px 0px 30px 0px rgba(0, 42, 106, 0.1);
        padding: 2rem;
        margin: 0rem 1rem;
        background-color: #fff;
        height: 400px;
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

</style>