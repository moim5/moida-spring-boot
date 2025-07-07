let scrollY;

function showBackdrop() {
    const backdrop = document.querySelector(".backdrop");
    const scrollBarWidth = window.innerWidth - document.documentElement.clientWidth;

    if (backdrop) {
        backdrop.classList.add("active");

        scrollY = window.scrollY;
        document.body.style.position = "fixed";
        document.body.style.top = `-${scrollY}px`;
        document.body.style.left = "0";
        document.body.style.right = "0";
        document.body.style.overflow = "hidden";
        document.body.style.paddingRight = `${scrollBarWidth}px`;
    }
}

function hideBackdrop() {
    const backdrop = document.querySelector(".backdrop");
    if (backdrop) {
        backdrop.classList.remove("active");

        document.body.style.position = "";
        document.body.style.top = "";
        document.body.style.left = "";
        document.body.style.right = "";
        document.body.style.overflow = "";
        window.scrollTo(0, scrollY);
        document.body.style.paddingRight = "";
    }
}

function showMenu() {
    const menuWrapper = document.querySelector(".menu-wrapper");

    if (menuWrapper) {
        menuWrapper.classList.add("active");
        showBackdrop();

    }
}

function hideMenu() {
    const menuWrapper = document.querySelector(".menu-wrapper");
    if (menuWrapper) {
        menuWrapper.classList.remove("active");
        hideBackdrop();
    }
}

document.addEventListener("click", function (event) {
    const menuWrapper = document.querySelector(".menu-wrapper");
    const menuContainer = document.querySelector(".menu-container");
    const menuButton = document.querySelector(".header-button[onclick='showMenu()']")

    if (menuWrapper && menuWrapper.classList.contains("active")) {
        if (!menuContainer.contains(event.target) && !menuButton.contains(event.target)) {
            hideMenu();
        }
    }
})

function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            console.log(data);
            document.getElementById("zipCode").value = data.zonecode;
            document.getElementById("address1").value = data.roadAddress;
        },
        width: "100%",
        height: "100%"
    }).open();
}