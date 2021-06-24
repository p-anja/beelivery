<template>
    <div id="sidebar">
        <ul id="sidebar-list">
            <li><slot></slot></li>
        </ul>
    </div>
</template>

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

    li {
        border-bottom: solid 1px #eee;
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
    #sidebar-list .router-link-exact-active
    {
        color: #fff;
        font-weight: 500;
        background-color: var(--accent);
        border-right: 2px solid var(--accent);
    }
    
    hr
    {
        width: 70%;
        margin: 0 auto;
    }
</style>