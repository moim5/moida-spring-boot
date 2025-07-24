//후기사진 클릭 시 확대
const img = document.querySelector('.review-img');

img.addEventListener('click', () => {
  img.classList.toggle('zoomed');
});