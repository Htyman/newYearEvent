(function () {
    document.addEventListener('DOMContentLoaded', () => {

        // ---- ЛОГАУТ (кука с JWT) ----

        const logoutButtons = document.querySelectorAll('[data-logout-button]');
        const loginStatus = document.querySelector('[data-login-status]'); // есть только на админке

        function setStatus(text) {
            if (loginStatus) {
                loginStatus.textContent = text;
            }
        }

        if (logoutButtons && logoutButtons.length > 0) {
            logoutButtons.forEach((btn) => {
                btn.addEventListener('click', async () => {
                    try {
                        await fetch('/api/auth/logout', {
                            method: 'POST',
                            credentials: 'same-origin',
                            headers: {
                                'X-Requested-With': 'XMLHttpRequest'
                            }
                        });
                    } catch (err) {
                        console.error('Ошибка при logout', err);
                    }
                    // После логаута редиректим на страницу входа
                    window.location.href = '/login';
                    // Если больше нравится просто перезагрузка:
                    // window.location.reload();
                });
            });
        }

        // ---- ГОЛОСОВАНИЕ /api/votes ----

        const voteForm = document.querySelector('[data-vote-form]');
        if (voteForm) {
            voteForm.addEventListener('submit', async (e) => {
                e.preventDefault();

                const nominationId = voteForm.getAttribute('data-nomination-id');
                const candidateInput = voteForm.querySelector('input[name="candidateId"]:checked');
                const messageBox = document.querySelector('[data-vote-message]');

                if (messageBox) messageBox.textContent = '';

                if (!candidateInput) {
                    if (messageBox) messageBox.textContent = 'Выберите кандидата.';
                    return;
                }

                if (!nominationId) {
                    if (messageBox) messageBox.textContent = 'Ошибка: не найдена номинация.';
                    return;
                }

                const candidateId = parseInt(candidateInput.value, 10);

                try {
                    const resp = await fetch('/api/votes', {
                        method: 'POST',
                        credentials: 'same-origin',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            nominationId: parseInt(nominationId, 10),
                            candidateId: candidateId
                        })
                    });

                    if (resp.ok) {
                        if (messageBox) messageBox.textContent = 'Голос засчитан!';
                        setTimeout(() => window.location.reload(), 700);
                    } else if (resp.status === 401 || resp.status === 403) {
                        if (messageBox) messageBox.textContent = 'Нужно войти перед голосованием.';
                    } else {
                        let msg = 'Ошибка голосования.';
                        try {
                            const data = await resp.json();
                            if (data && data.message) {
                                msg = data.message;
                            }
                        } catch (ignore) {}
                        if (messageBox) messageBox.textContent = msg;
                    }
                } catch (err) {
                    console.error(err);
                    if (messageBox) messageBox.textContent = 'Ошибка сети при голосовании.';
                }
            });
        }

        // ---- ОТМЕНА ГОЛОСА ----

        const cancelVoteBtn = document.querySelector('[data-cancel-vote]');
        if (cancelVoteBtn) {
            cancelVoteBtn.addEventListener('click', async () => {
                const nominationId = cancelVoteBtn.getAttribute('data-nomination-id');
                if (!nominationId) return;

                const messageBox = document.querySelector('[data-vote-message]');
                if (messageBox) messageBox.textContent = '';

                try {
                    const resp = await fetch(`/api/votes/${nominationId}`, {
                        method: 'DELETE',
                        credentials: 'same-origin',
                        headers: {
                            'X-Requested-With': 'XMLHttpRequest'
                        }
                    });

                    if (resp.ok || resp.status === 204) {
                        if (messageBox) messageBox.textContent = 'Голос отменён.';
                        setTimeout(() => window.location.reload(), 700);
                    } else if (resp.status === 401 || resp.status === 403) {
                        if (messageBox) messageBox.textContent = 'Нужно войти перед отменой голоса.';
                    } else {
                        if (messageBox) {
                            messageBox.textContent = 'Ошибка при отмене голоса. Код: ' + resp.status;
                        }
                    }
                } catch (err) {
                    console.error(err);
                    const messageBox2 = document.querySelector('[data-vote-message]');
                    if (messageBox2) messageBox2.textContent = 'Ошибка сети при отмене голоса.';
                }
            });
        }

    });
})();
