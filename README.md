## Bonus Task: Caching Layer (Simple In-Memory Cache)

### Goal
To improve performance, I implemented a **simple in-memory caching layer** that stores results of frequently called methods. This reduces repeated database queries and makes API responses faster. :contentReference[oaicite:0]{index=0}

---

### What Was Implemented
- **SimpleCache (Singleton)** — one shared cache instance across the whole application. :contentReference[oaicite:1]{index=1}  
- Cache storage is based on a **Map** (in-memory).
- The cache is used inside the **Service layer** to keep layered architecture clean. :contentReference[oaicite:2]{index=2}  

---

### Cached Method
I cached the result of a commonly used method:
- `CharacterService.getAllCharacters()` (GET all characters)

#### How it works
1. Service checks the cache by key `characters:all`
2. If cached data exists → it returns cached list (no DB call)
3. If not cached → it loads from repository (DB), saves into cache, and returns it

---

### Cache Invalidation Strategy
To prevent stale data, the cache is **invalidated** after any database modification. :contentReference[oaicite:3]{index=3}  

Cache invalidation happens after:
- `createCharacter(...)`
- `updateCharacter(...)`
- `deleteCharacter(...)`

This ensures consistency between cache and database.

---

### Why Cache Is Implemented in Service Layer
Caching is an optimization / business-level concern, so it belongs to the **Service layer**, not in the Repository layer.  
Repository should only focus on database access, while Service manages logic, caching, and validation. :contentReference[oaicite:4]{index=4}  

---

### Why Singleton Cache
The cache must be shared across the whole application, so I implemented it as a **Singleton**:
- only one cache instance exists
- consistent cache state for all requests
- controlled memory usage :contentReference[oaicite:5]{index=5}  

---

### Example (Demonstration)
1. Send GET `/api/characters` → first request loads from DB and caches the result.
2. Send GET `/api/characters` again → returns cached data without DB query.
3. Send POST/PUT/DELETE (modifies DB) → cache is invalidated.
4. Next GET `/api/characters` → loads fresh data and updates cache.

---

### Summary
This bonus feature demonstrates:
- performance optimization using caching
- correct cache invalidation
- clean architecture (cache in service layer)
- Singleton design pattern usage for shared cache instance :contentReference[oaicite:6]{index=6}
