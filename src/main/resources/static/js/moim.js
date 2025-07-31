async function showAttendeeListModal(moimId) {
    try {
        const response = await fetch("/moim/attendee/list", {
            method: "POST",
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({moimId})
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
                <button onclick="hideModal()">닫기</button>
            `
        });
    } catch {
        alert("알 수 없는 오류가 발생했습니다.")
    }
}