<template>
    <div id="sidebar">
        <b id="sidebar-title">{{title}}</b>
        <ul id="sidebar-list">
            <li><slot></slot></li>
        </ul>
    </div>
</template>

<script>
    module.exports = {
        props: ["title"],
    };
</script>

<style>
    #sidebar
    {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        height: 100%;
        z-index: 1;
        top: 0;
        left: 0;
        background-color: #fff; /* #fff;*/
        overflow-x: hidden;
        width: 10vw;
    }

    #sidebar-title
    {
        display: block;
        color: #222;
        font-weight: 500;
        font-size: 1.5rem;
        text-align: center;
        padding: 8px 16px 8px 16px;
        font-family: "Poppins", sans-serif;
        font-weight: 400;
        border-bottom: solid 1px #eee;
        margin-bottom: 15px;
    }

    li a {
        text-decoration: none;
    }
    ul {
        margin: 0;
        padding: 0 !important;
        list-style-type: none;
    }
    #sidebar-list a
    {
        display: block;
        color: #666;
        padding: 4px 8px 4px 8px;
        background-color: #fff;
    }
    #sidebar-list a:hover
    {
        color: #fff;
        font-weight: 500;
        background-color: var(--accent);
    }
    #sidebar-list .router-link-active
    {
        color: #111;
        font-weight: 500;
        background-color: #fff;
        border-right: 2px solid var(--accent);
    }
    
    hr
    {
        width: 70%;
        margin: 0 auto;
    }
</style>