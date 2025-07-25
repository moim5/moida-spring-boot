//삭제확인 모달
function closeModal(id) {
     document.getElementById(id).style.display = "none";
 }

 //2차 모달 띄우기
 function moimDeleteModal() {
     closeModal('confirmModal'); //1차 모달 닫기
     document.getElementById('moimDeleteModal').style.display = "flex";
 }



const moimDeleteModal = `
    <p>모임을 삭제하시겠습니까?</p>
`;

const moimDeleteModalFooter = `
    <button onclick="showMoimDeleteModal()">네</button>
    <button id="closeModal" onclick="closeModal('confirmModal')">아니오</button>
`;

function showMoimDeleteModal() {
    showModal({body: moimDeleteModalBody, footer: moimDeleteModalFooter});
}