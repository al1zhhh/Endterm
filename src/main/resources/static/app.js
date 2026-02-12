const API = ""; // same host

const alertHost = document.getElementById("alertHost");
const typeSel = document.getElementById("type");

const charactersTbody = document.getElementById("charactersTbody");
const guildsPre = document.getElementById("guildsPre");

const editModalEl = document.getElementById("editModal");
const editModal = new bootstrap.Modal(editModalEl);
const editJsonEl = document.getElementById("editJson");
const editPathEl = document.getElementById("editPath");
let editingId = null;

function showAlert(message, kind = "success") {
  const id = "a" + Math.random().toString(16).slice(2);
  const html = `
    <div class="alert alert-${kind} alert-dismissible fade show glass" role="alert" id="${id}">
      <div class="mono small" style="white-space:pre-wrap">${escapeHtml(message)}</div>
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>`;
  alertHost.insertAdjacentHTML("afterbegin", html);
  setTimeout(() => {
    const el = document.getElementById(id);
    if (el) el.remove();
  }, 6000);
}

function escapeHtml(s) {
  return String(s)
    .replaceAll("&","&amp;")
    .replaceAll("<","&lt;")
    .replaceAll(">","&gt;");
}

async function apiFetch(path, options = {}) {
  const res = await fetch(API + path, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options
  });

  const contentType = res.headers.get("content-type") || "";
  const text = await res.text();

  let data = null;
  if (text && contentType.includes("application/json")) {
    try { data = JSON.parse(text); } catch { data = null; }
  }

  if (!res.ok) {
    const details = data ? JSON.stringify(data, null, 2) : text;
    throw new Error(`${res.status} ${res.statusText}\n${details}`);
  }

  return data ?? text;
}

function toggleTypeFields() {
  const type = typeSel.value;

  document.querySelectorAll(".type-warrior").forEach(el => el.classList.toggle("d-none", type !== "WARRIOR"));
  document.querySelectorAll(".type-mage").forEach(el => el.classList.toggle("d-none", type !== "MAGE"));
  document.querySelectorAll(".type-rogue").forEach(el => el.classList.toggle("d-none", type !== "ROGUE"));
}

typeSel.addEventListener("change", toggleTypeFields);
toggleTypeFields();

function buildPayloadFromCreateForm() {
  const type = document.getElementById("type").value;
  const payload = {
    type,
    name: document.getElementById("name").value.trim(),
    level: Number(document.getElementById("level").value),
    experience: Number(document.getElementById("experience").value),
  };

  if (type === "WARRIOR") {
    payload.strength = Number(document.getElementById("strength").value);
    payload.armor = Number(document.getElementById("armor").value);
    payload.weaponType = document.getElementById("weaponType").value.trim();
  } else if (type === "MAGE") {
    payload.mana = Number(document.getElementById("mana").value);
    payload.intelligence = Number(document.getElementById("intelligence").value);
    payload.spellSchool = document.getElementById("spellSchool").value.trim();
  } else {
    payload.agility = Number(document.getElementById("agility").value);
    payload.stealth = Number(document.getElementById("stealth").value);
    payload.criticalChance = Number(document.getElementById("criticalChance").value);
  }

  return payload;
}

function rowHtml(c) {
  const type = c.type ?? c.characterType ?? "";
  return `
    <tr>
      <td class="mono">${c.id ?? ""}</td>
      <td>${escapeHtml(c.name ?? "")}</td>
      <td><span class="badge text-bg-secondary">${escapeHtml(type)}</span></td>
      <td>${c.level ?? ""}</td>
      <td>${c.experience ?? ""}</td>
      <td class="text-end">
        <div class="btn-group btn-group-sm">
          <button class="btn btn-outline-light" data-action="edit" data-id="${c.id}">Edit</button>
          <button class="btn btn-outline-danger" data-action="del" data-id="${c.id}">Delete</button>
        </div>
      </td>
    </tr>
  `;
}

function renderCharacters(list) {
  if (!Array.isArray(list) || list.length === 0) {
    charactersTbody.innerHTML = `<tr><td colspan="6" class="muted">No characters</td></tr>`;
    return;
  }
  charactersTbody.innerHTML = list.map(rowHtml).join("");
}

async function loadCharacters() {
  try {
    const data = await apiFetch("/api/characters");
    renderCharacters(data);
  } catch (e) {
    showAlert(e.message || String(e), "danger");
    charactersTbody.innerHTML = `<tr><td colspan="6" class="muted">Failed to load</td></tr>`;
  }
}

async function loadCharacterById(id) {
  const c = await apiFetch(`/api/characters/${id}`);
  renderCharacters([c]);
}

async function createCharacter() {
  try {
    const payload = buildPayloadFromCreateForm();
    const created = await apiFetch("/api/characters", {
      method: "POST",
      body: JSON.stringify(payload)
    });
    showAlert("Created:\n" + JSON.stringify(created, null, 2), "success");
    await loadCharacters();
  } catch (e) {
    showAlert(e.message || String(e), "danger");
  }
}

async function deleteCharacter(id) {
  if (!confirm(`Delete character ${id}?`)) return;
  try {
    await apiFetch(`/api/characters/${id}`, { method: "DELETE" });
    showAlert(`Deleted character ${id}`, "success");
    await loadCharacters();
  } catch (e) {
    showAlert(e.message || String(e), "danger");
  }
}

async function openEdit(id) {
  try {
    const c = await apiFetch(`/api/characters/${id}`);
    editingId = id;
    editPathEl.textContent = `/api/characters/${id}`;
    editJsonEl.value = JSON.stringify(c, null, 2);
    editModal.show();
  } catch (e) {
    showAlert(e.message || String(e), "danger");
  }
}

async function saveEdit() {
  if (editingId == null) return;
  try {
    const payload = JSON.parse(editJsonEl.value);

    // минимальная защита
    if (!payload.type) throw new Error("JSON must include field: type (WARRIOR/MAGE/ROGUE)");
    if (!payload.name) throw new Error("JSON must include field: name");
    if (!payload.level) throw new Error("JSON must include field: level");
    if (payload.experience === undefined) throw new Error("JSON must include field: experience");

    const updated = await apiFetch(`/api/characters/${editingId}`, {
      method: "PUT",
      body: JSON.stringify(payload)
    });

    showAlert("Updated:\n" + JSON.stringify(updated, null, 2), "success");
    editModal.hide();
    await loadCharacters();
  } catch (e) {
    showAlert(e.message || String(e), "danger");
  }
}

async function loadGuilds() {
  try {
    const data = await apiFetch("/api/guilds");
    guildsPre.textContent = JSON.stringify(data, null, 2);
  } catch (e) {
    showAlert(e.message || String(e), "danger");
    guildsPre.textContent = "Failed to load";
  }
}

async function createGuild() {
  const name = document.getElementById("guildName").value.trim();
  if (!name) return showAlert("Guild name is required", "warning");

  try {
    const created = await apiFetch("/api/guilds", {
      method: "POST",
      body: JSON.stringify({ name })
    });
    showAlert("Guild created:\n" + JSON.stringify(created, null, 2), "success");
    await loadGuilds();
  } catch (e) {
    showAlert(e.message || String(e), "danger");
  }
}

// events
document.getElementById("createForm").addEventListener("submit", (e) => {
  e.preventDefault();
  createCharacter();
});

document.getElementById("loadBtn").addEventListener("click", loadCharacters);

document.getElementById("loadByIdBtn").addEventListener("click", async () => {
  const id = document.getElementById("byId").value.trim();
  if (!id) return loadCharacters();
  try {
    await loadCharacterById(id);
  } catch (e) {
    showAlert(e.message || String(e), "danger");
  }
});

document.getElementById("refreshAllBtn").addEventListener("click", async () => {
  await loadCharacters();
  await loadGuilds();
  showAlert("Refreshed", "info");
});

document.getElementById("saveEditBtn").addEventListener("click", saveEdit);

document.getElementById("createGuildBtn").addEventListener("click", createGuild);
document.getElementById("loadGuildsBtn").addEventListener("click", loadGuilds);

// table actions (event delegation)
charactersTbody.addEventListener("click", (e) => {
  const btn = e.target.closest("button[data-action]");
  if (!btn) return;
  const id = btn.getAttribute("data-id");
  const action = btn.getAttribute("data-action");

  if (action === "edit") openEdit(id);
  if (action === "del") deleteCharacter(id);
});

// init
loadCharacters();
loadGuilds();