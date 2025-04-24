# 🧠 Interview Plan Schema Redesign – Summary & Rationale

## 🎯 Goal

To improve the flexibility, traceability, and realism of AI-led interview planning for resume enrichment by updating the **interview plan schema**. This includes:
- Stable linking to resume data
- Better handling of gaps and transitions
- Supporting nonlinear interview flows
- Enabling structured prompt design and conversation tracking

---

## ✅ Key Problems Identified

### 1. **Unstable Resume References**
- Resume entries were referenced using mutable content fields (`name`, `startDate`).
- This created fragile links that could break on edit.

### 2. **Redundancy Between `type` and `reference.section`**
- Topics duplicated data by having both a `type` (e.g., `work_experience`) and a `section` (e.g., `work`), which always aligned.

### 3. **Gaps Were Underspecified**
- The existing gap model assumed time-based gaps with two dates.
- But some gaps are:
  - Only anchored on one side (timeline boundaries)
  - Not time-based at all (e.g. skill or leadership gaps)

### 4. **Threads Were Too Tightly Coupled**
- Threads relied on parent context and couldn't be used in isolation.
- Conversational threads needed more structured metadata for flexibility.

### 5. **Status Lacked Intermediate State**
- Current `status` enum didn’t support real-world interview interruptions (e.g., intent switching).

---

## ✅ Key Improvements and Decisions

### 🔹 Stable Resume References with NanoIDs
- Resume items now carry a stable `id` (generated via 8-char [NanoID](https://github.com/ai/nanoid)).
- Interview topics reference resume items using this `resumeItemId`.

### 🔹 Simplified and Centralized Topic Types
- Removed `reference.section` and rely solely on `type`.
- Types can now include both:
  - Resume-aligned types (`work_experience`, `project`)
  - Abstract types (`gap`, `career_transition`, `freelance_period`)

### 🔹 Structured Support for Gaps
- `gapType` introduced:
  - `"time"` for in-between gaps
  - `"timeline_boundary"` for open-ended gaps (e.g. “What did you do after 2022?”)
- Gaps now have:
  - `reference.startDate` and/or `reference.endDate`
  - Optional linkage to resume items (`resumeItemBeforeId`, etc.)
  - Human-readable `reason`
  - Machine-readable `reasonMeta` for diagnostics

### 🔹 Threads Are Now Standalone and Structured
- Each thread includes a `topicId` for full traceability
- Added optional fields:
  - `focus`: human-readable summary
  - `contextObject`, `contextAction`, `contextGoal`: structured elements for prompting
- Threads support `priorityScore`, `createdAt`, `updatedAt`

### 🔹 New Status: `on_hold`
- Added `"on_hold"` to the `status` enum to support:
  - Mid-interview topic switches
  - Temporary deferrals
  - Intent-driven flow control

---

## 🧩 Other Enhancements

- `meta` block at the top of the plan (with schema version, timestamps, generator info)
- All parts are extensible (`additionalProperties: true`)
- Design aligns with future graph-based conversation flows (e.g., LangGraph-style)

---

## 📘 Benefits of These Changes

- ✅ Robust references and resume alignment
- ✅ Richer, more flexible planning and prompting
- ✅ Real-world conversational behavior (pause, resume, switch)
- ✅ Better UX and agent capabilities
- ✅ Easier evolution, validation, and debugging
