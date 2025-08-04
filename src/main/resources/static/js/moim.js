async function showAttendeeListModal(moimId) {
    try {
        const response = await fetch("/moim/attendee/list", {
            method: "POST",
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({"moimId": moimId.toString()})
        });

        if (!response.ok) {
            alert("참가자 정보를 불러오는 데 실패했습니다.");
            return;
        }

        const attendees = await response.json();

        if (attendees.length === 0) {
            showModal({
                header: `참가자 목록`,
                body: `<p style="padding: 30px; text-align: center;">아직 참가자가 없습니다.</p>`,
                footer: `<div style="width: 100%; display: flex; justify-content: flex-end;">
                            <button class="ok-button" onclick="hideModal()">닫기</button>
                         </div>`
            });
            return;
        }

        const rows = attendees.map(attendee => `
            <tr>
                <td>${attendee.name}</td>
                <td>${attendee.email}</td>
                <td>${attendee.phone}</td>
            </tr>
        `).join("");

        const table = `
            <table>
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>전화번호</th>
                    </tr>
                </thead>
                <tbody>
                    ${rows}
                </tbody>
            </table>
        `;

        showModal({
            header: "참가자 목록",
            body: table,
            footer: `
                <div style="width: 100%; display: flex; justify-content: flex-end;">
                    <button class="ok-button" onclick="hideModal()">닫기</button>
                </div>
            `
        });
    } catch {
        alert("알 수 없는 오류가 발생했습니다.")
    }
}

async function createMoimAttendee(moimId) {
    const clickedButton = event.currentTarget;
    const context = clickedButton.dataset.context;

    try {
        const response = await fetch(`/moim/join/${moimId}`, {method: "POST"});
        const result = await response.text();

        if (result === "true") {
            updateJoinButtonUI(moimId, context, true, clickedButton);
        } else {
            alert("모임 참가 신청이 실패되었습니다. 다시 시도해주세요.");
        }
    } catch {
        alert("모임 참가 신청이 실패되었습니다.")
    }
}

async function cancelMoimAttendee(moimId) {
    const clickedButton = event.currentTarget;
    const context = clickedButton.dataset.context;

    try {
        const response = await fetch(`/moim/cancel/${moimId}`, {method: "POST"});
        const result = await response.text();

        if (result === "true") {
            updateJoinButtonUI(moimId, context, false, clickedButton);
        } else {
            alert("모임 취소 신청이 실패되었습니다. 다시 시도해주세요.");
        }
    } catch {
        alert("모임 취소 신청이 실패되었습니다.");
    }
}

function updateJoinButtonUI(moimId, context, isJoined, button) {
    if (context === "detail") {
        const buttons = document.querySelectorAll(
            `button[data-moim-id="${moimId}"][data-context="detail"]`
        );

        buttons.forEach((btn) => {
            btn.textContent = isJoined ? "참가취소" : "신청하기";
            btn.classList.toggle("cancel-button", isJoined);
            btn.onclick = isJoined ? () => cancelMoimAttendee(moimId) : () => createMoimAttendee(moimId);
        });
    } else {
        button.textContent = isJoined ? '참가취소' : '재신청';
        button.className = isJoined ? "cancel-button" : "modify-button";
        button.removeAttribute("onclick");
        button.onclick = isJoined
            ? () => cancelMoimAttendee(moimId)
            : () => createMoimAttendee(moimId);
    }
}

async function cancelMoim(moimId) {
	const confirmDelete = confirm("정말 이 모임을 삭제하시겠습니까?");
	if (!confirmDelete) {
	    return;
	}
    try {
        const response = await fetch(`/moim/cancelMoim/${moimId}`, {
            method: 'POST',
			headers: {
			     "Content-type": "application/x-www-form-urlencoded",
			 },
			body: new URLSearchParams({"moimId": moimId.toString()})
        });
		
		const result = await response.text();
		if(result == "false") {
			return alert("삭제 실패: " + err);
		}
		
		location.reload(true);
    } catch (error) {
        console.error("삭제 중 오류:", error);
        alert("서버 오류가 발생했습니다.");
    }
}
