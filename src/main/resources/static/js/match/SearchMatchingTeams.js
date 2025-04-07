function searchTeams() {
    const searchTeamName = document.getElementById("teamSearchInput").value.trim();
    const container = document.getElementById("teamListContainer");

    fetch(`/teams/searchMatchingTeams?teamName=${encodeURIComponent(searchTeamName)}`)
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    container.innerHTML = `<p class="text-center text-red-500">${error.searchTeamName}</p>`;
                    console.log(error)
                    throw new Error(error.searchTeamName);
                });
            }
            return response.json();
        })
        .then(data => {

            let html = `
                <table class="w-auto border border-gray-300 mt-4 text-sm">
                    <thead class="bg-gray-100">
                        <tr>
                            <th class="px-2 py-1 border whitespace-nowrap">팀명</th>
                            <th class="px-2 py-1 border whitespace-nowrap">팀장 이름</th>
                            <th class="px-2 py-1 border whitespace-nowrap">팀장 ID</th>
                            <th class="px-2 py-1 border whitespace-nowrap">지 역</th>
                            <th class="px-2 py-1 border whitespace-nowrap">나이대</th>
                            <th class="px-2 py-1 border whitespace-nowrap text-center">선택</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            for (const team of data) {
                html += `
                    <tr class="hover:bg-gray-100">
                        <td class="py-2 px-4 border">${team.teamName}</td>
                        <td class="py-2 px-4 border">${team.leaderName}</td>
                        <td class="py-2 px-4 border">${team.leaderEmail}</td>
                        <td class="py-2 px-4 border">${team.region}</td>
                        <td class="py-2 px-4 border">${team.ageRange}</td>
                        <td class="py-2 px-4 border text-center">
                            <button onclick="selectTeam('${team.teamName}', '${team.leaderName}', '${team.leaderEmail}'
                            , '${team.region}', '${team.ageRange}')"
                                    class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600 text-sm">
                                선택
                            </button>
                        </td>
                    </tr>
                `;
            }

            html += "</tbody></table>";
            container.innerHTML = html;
        })
        .catch(error => {
            container.innerHTML = `<p class="text-center text-red-500">${error.message}</p>`;
        });
}

function selectTeam(teamName, leaderName, leaderEmail, region, ageRange) {
    const opposingTeamInfo = document.getElementById("opposingTeamInfo");
    const inviteeEmail = document.getElementById("inviteeEmail");
    const inviteeTeamName = document.getElementById("inviteeTeamName");

    opposingTeamInfo.value = `${teamName} / ${leaderName} / ${leaderEmail} / ${region} / ${ageRange}`;
    opposingTeamInfo.title = `${teamName} / ${leaderName} / ${leaderEmail} / ${region} / ${ageRange}`;
    inviteeEmail.value = leaderEmail;
    inviteeTeamName.value = teamName;

    opposingTeamInfo.setAttribute("disabled", true);

    toggleModal('searchModal', false);
}