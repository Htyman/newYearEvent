(function () {

    const adminMessageBox = document.querySelector('[data-admin-message]');

    function setAdminMessage(text, isError = false) {
        if (adminMessageBox) {
            adminMessageBox.textContent = text;
            adminMessageBox.className = 'message ' + (isError ? 'error' : 'success');
        }
    }

    // ---- ОТКРЫТЬ / ЗАКРЫТЬ НОМИНАЦИЮ ----

    const openButtons = document.querySelectorAll('[data-admin-open]');
    openButtons.forEach(btn => {
        btn.addEventListener('click', async () => {
            const id = btn.getAttribute('data-nomination-id');
            if (!id) return;

            try {
                const resp = await fetch(`/api/admin/nominations/${id}/open`, {
                    method: 'POST'
                });
                if (resp.ok) {
                    setAdminMessage(`Номинация ${id} открыта.`);
                    setTimeout(() => window.location.reload(), 500);
                } else if (resp.status === 401 || resp.status === 403) {
                    setAdminMessage('Нужно войти как админ.', true);
                } else {
                    setAdminMessage('Ошибка при открытии номинации.', true);
                }
            } catch (err) {
                console.error(err);
                setAdminMessage('Ошибка сети при открытии номинации.', true);
            }
        });
    });

    const closeButtons = document.querySelectorAll('[data-admin-close]');
    closeButtons.forEach(btn => {
        btn.addEventListener('click', async () => {
            const id = btn.getAttribute('data-nomination-id');
            if (!id) return;

            try {
                const resp = await fetch(`/api/admin/nominations/${id}/close`, {
                    method: 'POST'
                });
                if (resp.ok) {
                    setAdminMessage(`Номинация ${id} закрыта.`);
                    setTimeout(() => window.location.reload(), 500);
                } else if (resp.status === 401 || resp.status === 403) {
                    setAdminMessage('Нужно войти как админ.', true);
                } else {
                    setAdminMessage('Ошибка при закрытии номинации.', true);
                }
            } catch (err) {
                console.error(err);
                setAdminMessage('Ошибка сети при закрытии номинации.', true);
            }
        });
    });

    // ---- СОЗДАТЬ НОМИНАЦИЮ ----

    const createNominationForm = document.querySelector('[data-admin-create-nomination-form]');
    const createNominationMsg = document.querySelector('[data-admin-create-nomination-message]');

    if (createNominationForm) {
        createNominationForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            if (createNominationMsg) {
                createNominationMsg.textContent = '';
                createNominationMsg.className = 'message';
            }

            const name = createNominationForm.querySelector('input[name="name"]').value.trim();
            const description = createNominationForm.querySelector('textarea[name="description"]').value.trim();
            const photoUrl = createNominationForm.querySelector('input[name="photoUrl"]').value.trim();

            if (!name) {
                if (createNominationMsg) {
                    createNominationMsg.textContent = 'Название обязательно.';
                    createNominationMsg.className = 'message error';
                }
                return;
            }

            try {
                const resp = await fetch('/api/admin/nominations', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        name,
                        description: description || null,
                        photoUrl: photoUrl || null
                    })
                });

                if (resp.ok) {
                    if (createNominationMsg) {
                        createNominationMsg.textContent = 'Номинация создана.';
                        createNominationMsg.className = 'message success';
                    }
                    createNominationForm.reset();
                    setTimeout(() => window.location.reload(), 500);
                } else if (resp.status === 401 || resp.status === 403) {
                    if (createNominationMsg) {
                        createNominationMsg.textContent = 'Нужно войти как админ.';
                        createNominationMsg.className = 'message error';
                    }
                } else {
                    if (createNominationMsg) {
                        createNominationMsg.textContent = 'Ошибка при создании номинации.';
                        createNominationMsg.className = 'message error';
                    }
                }
            } catch (err) {
                console.error(err);
                if (createNominationMsg) {
                    createNominationMsg.textContent = 'Ошибка сети при создании номинации.';
                    createNominationMsg.className = 'message error';
                }
            }
        });
    }

    // ---- СОЗДАТЬ КАНДИДАТА ----

    const createCandidateForm = document.querySelector('[data-admin-create-candidate-form]');
    const createCandidateMsg = document.querySelector('[data-admin-create-candidate-message]');

    if (createCandidateForm) {
        createCandidateForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            if (createCandidateMsg) {
                createCandidateMsg.textContent = '';
                createCandidateMsg.className = 'message';
            }

            const name = createCandidateForm.querySelector('input[name="name"]').value.trim();
            const surname = createCandidateForm.querySelector('input[name="surname"]').value.trim();
            const photoUrl = createCandidateForm.querySelector('input[name="photoUrl"]').value.trim();
            const description = createCandidateForm.querySelector('textarea[name="description"]').value.trim();

            if (!name) {
                if (createCandidateMsg) {
                    createCandidateMsg.textContent = 'Имя обязательно.';
                    createCandidateMsg.className = 'message error';
                }
                return;
            }

            try {
                const resp = await fetch('/api/admin/candidates', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        name,
                        surname: surname || null,
                        photoUrl: photoUrl || null,
                        description: description || null
                    })
                });

                if (resp.ok) {
                    if (createCandidateMsg) {
                        createCandidateMsg.textContent = 'Кандидат создан.';
                        createCandidateMsg.className = 'message success';
                    }
                    createCandidateForm.reset();
                } else if (resp.status === 401 || resp.status === 403) {
                    if (createCandidateMsg) {
                        createCandidateMsg.textContent = 'Нужно войти как админ.';
                        createCandidateMsg.className = 'message error';
                    }
                } else {
                    if (createCandidateMsg) {
                        createCandidateMsg.textContent = 'Ошибка при создании кандидата.';
                        createCandidateMsg.className = 'message error';
                    }
                }
            } catch (err) {
                console.error(err);
                if (createCandidateMsg) {
                    createCandidateMsg.textContent = 'Ошибка сети при создании кандидата.';
                    createCandidateMsg.className = 'message error';
                }
            }
        });
    }

    // ---- ПРИВЯЗАТЬ КАНДИДАТА К НОМИНАЦИИ ----

    const linkForm = document.querySelector('[data-admin-link-form]');
    const linkMsg = document.querySelector('[data-admin-link-message]');

    if (linkForm) {
        linkForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            if (linkMsg) {
                linkMsg.textContent = '';
                linkMsg.className = 'message';
            }

            const nominationId = linkForm.querySelector('input[name="nominationId"]').value.trim();
            const candidateId = linkForm.querySelector('input[name="candidateId"]').value.trim();

            if (!nominationId || !candidateId) {
                if (linkMsg) {
                    linkMsg.textContent = 'Укажите ID номинации и кандидата.';
                    linkMsg.className = 'message error';
                }
                return;
            }

            try {
                const resp = await fetch(`/api/admin/nominations/${nominationId}/candidates/${candidateId}`, {
                    method: 'POST'
                });

                if (resp.ok) {
                    if (linkMsg) {
                        linkMsg.textContent = 'Кандидат привязан к номинации.';
                        linkMsg.className = 'message success';
                    }
                    linkForm.reset();
                } else if (resp.status === 401 || resp.status === 403) {
                    if (linkMsg) {
                        linkMsg.textContent = 'Нужно войти как админ.';
                        linkMsg.className = 'message error';
                    }
                } else {
                    if (linkMsg) {
                        linkMsg.textContent = 'Ошибка при привязке кандидата.';
                        linkMsg.className = 'message error';
                    }
                }
            } catch (err) {
                console.error(err);
                if (linkMsg) {
                    linkMsg.textContent = 'Ошибка сети при привязке кандидата.';
                    linkMsg.className = 'message error';
                }
            }
        });
    }

})();
