<template>
    <div id="home-main">
        <h1>{{response}} from {{name}}</h1>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            name: 'Hello',
            response: '',
        }
    },

    methods: {
        getHello: function() {
            axios.get('hello')
                .then(r => this.response = r.data)
                .catch(r => console.log(r));
        },
    },

    mounted() {
        this.getHello();
    }
};
</script>