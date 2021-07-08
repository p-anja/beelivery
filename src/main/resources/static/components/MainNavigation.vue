<template>
	<div id="navapp">
		<nav id="navbar">
			<div id="nav" :class="{sticky:active}">
				<div class="nav-container" :class="toggleNavClass()">
					<div class="navbar-item logo">
						<img src="img/beelogo.png" alt="bee" width="32px" height="32px">
						<router-link id="logo" to="/">beelivery</router-link>
					</div>
					<div id="nav-search">
						<!-- Search text field -->
						<div id="nav-search-container">
							<input placeholder="Search restaurants" type="text" v-model="search" @keyup.enter="$router.push('/restsearch/' + search)">
							<span class="border"></span>
						</div>
					</div>
					<div id="nav-links">
						<slot></slot>
						<div id="user-controls" v-if="jws">
							<div class="dropdown">
								<router-link class="dropbtn" to="/userinfo">Profile</router-link>
								<div class="dropdown-content">
									<router-link to="/userinfo">Profile info</router-link>
									<router-link v-if="role == 'REGULAR'" to="/cart">Cart</router-link>
									<router-link to="/orders">Orders</router-link>
									<hr/>
									<a href="#" @click="logout">Logout</a>
								</div>
							</div>
						</div>
						<router-link v-else to="/signin">Login</router-link>
					</div>
				</div>
			</div>
		</nav>
	</div>
</template>
<script>
	module.exports =
	{
		data: function()
		{
			return{
				search: '',
				active: false,
				role: '',
			}

		},
		methods:
		{
			getRole: function() {
				if(!localStorage.jws) {
					return;
				}
				axios.get('/user/role', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
					.then(r => this.role = r.data);
			},
			logout: function() {
				localStorage.removeItem('jws');
				this.$router.go();
			},

			toggleNavClass()
			{
				if(this.active == false)
				{
					return "navbar";
				}
					else
				{
					return "navbar-sticky";
				}
			},
		},
		computed:
		{
			jws: function() {
				return localStorage.jws;
			}
		},
        mounted()
        {
			this.getRole();
        },
	}
</script>
<style scoped>
	#logo {
		text-decoration: none;
		color: #311403;
	}

	#logo:hover {
		color: var(--accent);
	}

	#navapp {
		--h: 8vh;
		background-color: #fff;
	}

	.navbar {
		transition: 100ms;
		padding: 0px 25px 0px 25px;
	}

	.navbar-sticky {
		transition: 100ms;
		padding: 0px 25px 0px 25px;
	}

	#nav {
		transition: 150ms;
		height: var(--h);
		width: 100%;
		top: 0;
		z-index: 3;
	}

	#nav.sticky {
		transition: 150ms;
		background-color: #fff;
		box-shadow: 0px 1px 10px #999;
	}

	.nav-container {
		display: flex;
		flex-direction: row;
		/* flex-wrap: wrap; */
		align-items: center;
		justify-content: space-between;
	}

	.sticky > .nav-container {
		flex-wrap: nowrap;
	}

	.nav-container > * {
		min-height: var(--h);
		flex-basis: 50%;
	}


	.navbar-dropdown:hover .navbar-list {
		display: block;
		top: 7vh;
	}

	#nav-links {
		display: flex;
		flex-direction: row;
		align-content: center;
		justify-content: flex-end;
	}

	#nav-links a {
		display: grid;
		place-items: center;
		color: #311403;
		text-decoration: none;
		text-align: center;
		font-size: 1.5rem;
		font-weight: 400;
		padding: 5px;
	}

	#nav-links a:hover {
		color: var(--accent);
	}

	.navbar-item {
		margin: 0;
		padding: 0;
	}

	.logo {
		flex-grow: 1;
		display: flex;
		align-items: center;
	}

	.navbar-sticky > .logo {
		max-width: 13rem;
	}

	.nav-container {
		max-width: 64vw;
		margin: auto;
	}

	#nav > .nav-container > .logo {
		font-family: "Poppins";
		font-size: 1.5rem;
		border: none;
		color: #311403;
		text-transform: uppercase;
		font-weight: 600;
	}

	.navbar-list hr {
		padding: 0px;
		margin: 0px;
	}

	#nav.sticky > .nav-container > .logo{
		font-size: 1.8rem;
		border: none;
		color: #e12454;
		text-transform: uppercase;
		font-weight: 600;
	}

	#nav-search {
		display: grid;
		place-items: center;
	}

	#nav-search-container {
		position: relative;
		display: flex;
		flex-direction: column;
		justify-content: flex-end;
	}

	#nav-search-container input {
		border: 0px;
		border-bottom: solid 1px #eee;
		padding: 0px;
		margin: 0px;
	}

	#nav-search-container input ~ .border {
		position: absolute;
		bottom: 0;
		left: 0;
		width: 0;
	}

	#nav-search input:focus ~ .border {
		width: 100%;
		transition: 0.5s;
		border: solid 1px var(--accent);
	}

	.border {
		border: 0px;
	}

	#user-controls {
		display: flex;
		flex-direction: row;
	}

	.dropdown {
		position:relative;
		display: inline-block;
	}

	.dropdown-content {
		display: none;
		position: absolute;
		background-color: #fff;
		min-width: 160px;
		box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
		z-index: 99;
	}

	.dropdown-content hr {
		width: 90%;
		margin: 0 auto;
		color: #eee;
	}

	#nav-links .dropdown .dropdown-content a:hover {
		color: #fff;
		font-weight: 500;
		background: #e12454;
	}


	.dropdown:hover .dropdown-content {
		display: block;
	}

	#nav-links .dropdown .dropbtn {
		padding-bottom: 0;
		padding-top: 0;
	}
	
	.dropbtn {
		content: '';
		display: inline-block;
		vertical-align: middle;
		height: 100%;
	}

</style>