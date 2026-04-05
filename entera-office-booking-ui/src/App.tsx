import React, { useEffect, useMemo, useState } from "react";

const API_BASE_URL = "http://localhost:9010";
const GREEN_COLOR = "#00b482";
const WHITE_COLOR = "#ffffff";
const DARK_GREEN = "#00956c";
const LIGHT_GREEN = "#e9fff8";
const BORDER_COLOR = "#d8f5ec";
const TEXT_PRIMARY = "#12332a";
const TEXT_SECONDARY = "#5e7e75";
const PAGE_BACKGROUND = "#f4fbf8";
const DANGER_BG = "#fff1f1";
const DANGER_TEXT = "#c55050";

type Area = {
  id: string;
  name: string;
};

type Desk = {
  id: string;
  number: number;
  area?: Area | null;
};

type Booking = {
  id: string;
  date?: string;
  user?: {
    id: string;
    firstName?: string;
    lastName?: string;
    email?: string;
  } | null;
  desk?: Desk | null;
};

type AuthResponse = {
  token: string;
  role: string;
};

type Page = "admin" | "booking";

async function request<T>(path: string, token?: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options?.headers ?? {}),
    },
    ...options,
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `HTTP ${response.status}`);
  }

  const contentType = response.headers.get("content-type") || "";
  if (!contentType.includes("application/json")) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export default function App() {
  const [page, setPage] = useState<Page>("booking");

  const [token, setToken] = useState(() => localStorage.getItem("jwt") || "");
  const [loginEmail, setLoginEmail] = useState("");
  const [loginPassword, setLoginPassword] = useState("");
  const [role, setRole] = useState(() => localStorage.getItem("role") || "")
  const isAdmin = role === "ADMIN";
  const isAuthenticated = Boolean(token);

  const [areas, setAreas] = useState<Area[]>([]);
  const [desks, setDesks] = useState<Desk[]>([]);
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [newAreaName, setNewAreaName] = useState("");
  const [deskAreaId, setDeskAreaId] = useState("");
  const [deskNumber, setDeskNumber] = useState("");

  const [selectedDeskId, setSelectedDeskId] = useState("");
  const [userId, setUserId] = useState("");
  const [bookingDate, setBookingDate] = useState(() => new Date().toISOString().slice(0, 10));

  const selectedDesk = useMemo(
      () => desks.find((desk) => desk.id === selectedDeskId) ?? null,
      [desks, selectedDeskId]
  );

  const filteredDesksForSelectedArea = useMemo(
      () => desks.filter((desk) => desk.area?.id === deskAreaId),
      [desks, deskAreaId]
  );

  async function loadAll(currentToken = token) {
    if (!currentToken) {
      return;
    }

    try {
      setLoading(true);
      setError("");

      const [areasData, desksData, bookingsData] = await Promise.all([
        request<Area[]>("/areas", currentToken),
        request<Desk[]>("/desks", currentToken),
        request<Booking[]>("/bookings", currentToken),
      ]);

      setAreas(areasData ?? []);
      setDesks(desksData ?? []);
      setBookings(bookingsData ?? []);

      if (!deskAreaId && areasData?.length) {
        setDeskAreaId(areasData[0].id);
      }
      if (!selectedDeskId && desksData?.length) {
        setSelectedDeskId(desksData[0].id);
      }
    } catch (e) {
      const message = getErrorMessage(e);
      setError(message);

      if (message.includes("401") || message.includes("403")) {
        logout(false);
      }
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (token) {
      loadAll(token);
    }
  }, [token]);

  async function login() {
    if (!loginEmail.trim() || !loginPassword.trim()) {
      setError("Введите email и пароль");
      return;
    }

    try {
      setLoading(true);
      setError("");
      setSuccess("");

      const response = await request<AuthResponse>("/auth/login", undefined, {
        method: "POST",
        body: JSON.stringify({
          email: loginEmail.trim(),
          password: loginPassword,
        }),
      });

      setToken(response.token);
      setRole(response.role);
      localStorage.setItem("jwt", response.token);
      localStorage.setItem("role", response.role);
      setSuccess("Вход выполнен");
      setLoginPassword("");
    } catch (e) {
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }

  function logout(showMessage = true) {
    setToken("");
    localStorage.removeItem("jwt");
    localStorage.removeItem("role");
    setAreas([]);
    setDesks([]);
    setBookings([]);
    setSelectedDeskId("");
    setDeskAreaId("");
    setSuccess(showMessage ? "Вы вышли из системы" : "");
    setError("");
  }

  async function createArea() {
    if (!newAreaName.trim()) {
      setError("Введите название пространства");
      return;
    }

    try {
      setError("");
      setSuccess("");
      await request<Area>("/areas", token, {
        method: "POST",
        body: JSON.stringify({ id: null, name: newAreaName.trim() }),
      });
      setNewAreaName("");
      setSuccess("Пространство создано");
      await loadAll();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }

  async function deleteArea(id: string) {
    try {
      setError("");
      setSuccess("");
      await request<void>(`/areas/${id}`, token, { method: "DELETE" });
      setSuccess("Пространство удалено");
      await loadAll();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }

  async function createDesk() {
    if (!deskAreaId) {
      setError("Выберите пространство");
      return;
    }

    if (!deskNumber.trim()) {
      setError("Введите номер стола");
      return;
    }

    const parsedNumber = Number(deskNumber);
    if (Number.isNaN(parsedNumber)) {
      setError("Номер стола должен быть числом");
      return;
    }

    try {
      setError("");
      setSuccess("");
      await request<Desk>("/desks", token, {
        method: "POST",
        body: JSON.stringify({ id: null, areaId: deskAreaId, number: parsedNumber }),
      });
      setDeskNumber("");
      setSuccess("Стол создан");
      await loadAll();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }

  async function deleteDesk(id: string) {
    try {
      setError("");
      setSuccess("");
      await request<void>(`/desks/${id}`, token, { method: "DELETE" });
      setSuccess("Стол удалён");
      await loadAll();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }

  async function deleteBooking(id: string) {
    try {
      setError("");
      setSuccess("");
      await request<void>(`/bookings/${id}`, token, { method: "DELETE" });
      setSuccess("Бронирование удалено");
      await loadAll();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }

  async function createBooking() {
    if (!userId.trim()) {
      setError("Введите userId");
      return;
    }

    if (!selectedDeskId) {
      setError("Выберите стол");
      return;
    }

    try {
      setError("");
      setSuccess("");
      await request<Booking>("/bookings", token, {
        method: "POST",
        body: JSON.stringify({ userId: userId.trim(), deskId: selectedDeskId }),
      });
      setSuccess("Стол забронирован");
      await loadAll();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }

  if (!isAuthenticated) {
    return (
        <div style={styles.page}>
          <div style={styles.authShell}>
            <section style={styles.authCard}>
              <div style={styles.badge}>ENTERA OFFICE BOOKING</div>
              <h1 style={styles.title}>Вход в систему</h1>
              <p style={styles.subtitle}>
                Введите email и пароль, чтобы открыть интерфейс бронирования.
              </p>

              {error && <div style={styles.errorBanner}>{error}</div>}
              {success && <div style={styles.successBanner}>{success}</div>}

              <div style={styles.formColumn}>
                <input
                    value={loginEmail}
                    onChange={(e) => setLoginEmail(e.target.value)}
                    placeholder="Email"
                    style={styles.input}
                />

                <input
                    type="password"
                    value={loginPassword}
                    onChange={(e) => setLoginPassword(e.target.value)}
                    placeholder="Пароль"
                    style={styles.input}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        login();
                      }
                    }}
                />

                <button onClick={login} style={styles.bookButton} disabled={loading}>
                  {loading ? "Входим..." : "Войти"}
                </button>
              </div>
            </section>
          </div>
        </div>
    );
  }

  return (
      <div style={styles.page}>
        <div style={styles.shell}>
          <header style={styles.headerCard}>
            <div style={styles.headerTopRow}>
              <div>
                <div style={styles.badge}>ENTERA OFFICE BOOKING</div>
                <h1 style={styles.title}>Управление офисными столами</h1>
                <p style={styles.subtitle}>
                  Бронируй в два клика.
                </p>
              </div>

              <div style={styles.navGroup}>
                {isAdmin && (
                    <NavButton active={page === "admin"} onClick={() => setPage("admin")}>
                      Админка
                    </NavButton>
                )}
                <NavButton active={page === "booking"} onClick={() => setPage("booking")}>
                  Бронирование
                </NavButton>
                <button onClick={() => logout()} style={styles.dangerButton}>
                  Выйти
                </button>
              </div>
            </div>
          </header>

          <div style={styles.actionsRow}>
            <button onClick={() => loadAll()} style={styles.primaryButton}>
              Обновить данные
            </button>
            {loading && <span style={styles.loadingText}>Загрузка…</span>}
          </div>

          {error && <div style={styles.errorBanner}>{error}</div>}
          {success && <div style={styles.successBanner}>{success}</div>}

          {page === "admin" ? (
              <div style={styles.gridThree}>
                <Card title="Пространства">
                  <div style={styles.inlineForm}>
                    <input
                        value={newAreaName}
                        onChange={(e) => setNewAreaName(e.target.value)}
                        placeholder="Название пространства"
                        style={styles.input}
                    />
                    <button onClick={createArea} style={styles.primaryButton}>
                      Создать
                    </button>
                  </div>

                  <div style={styles.stackList}>
                    {areas.length === 0 && <EmptyState text="Пространств пока нет" />}
                    {areas.map((area) => (
                        <div key={area.id} style={styles.itemCard}>
                          <div>
                            <div style={styles.itemTitle}>{area.name}</div>
                            <div style={styles.itemMeta}>{area.id}</div>
                          </div>
                          <button onClick={() => deleteArea(area.id)} style={styles.dangerButton}>
                            Удалить
                          </button>
                        </div>
                    ))}
                  </div>
                </Card>

                <Card title="Столы">
                  <div style={styles.formColumn}>
                    <select
                        value={deskAreaId}
                        onChange={(e) => setDeskAreaId(e.target.value)}
                        style={styles.input}
                    >
                      <option value="">Выбери пространство</option>
                      {areas.map((area) => (
                          <option key={area.id} value={area.id}>
                            {area.name}
                          </option>
                      ))}
                    </select>

                    <div style={styles.inlineForm}>
                      <input
                          value={deskNumber}
                          onChange={(e) => setDeskNumber(e.target.value)}
                          placeholder="Номер стола"
                          style={styles.input}
                      />
                      <button onClick={createDesk} style={styles.primaryButton}>
                        Создать
                      </button>
                    </div>
                  </div>

                  <div style={styles.stackList}>
                    {!deskAreaId && <EmptyState text="Сначала выбери пространство" />}
                    {deskAreaId && filteredDesksForSelectedArea.length === 0 && (
                        <EmptyState text="В этом пространстве пока нет столов" />
                    )}
                    {filteredDesksForSelectedArea.map((desk) => (
                        <div key={desk.id} style={styles.itemCard}>
                          <div>
                            <div style={styles.itemTitle}>Стол #{desk.number}</div>
                            <div style={styles.itemSecondary}>Пространство: {desk.area?.name ?? "—"}</div>
                            <div style={styles.itemMeta}>{desk.id}</div>
                          </div>
                          <button onClick={() => deleteDesk(desk.id)} style={styles.dangerButton}>
                            Удалить
                          </button>
                        </div>
                    ))}
                  </div>
                </Card>

                <Card title="Бронирования">
                  <div style={styles.stackList}>
                    {bookings.length === 0 && <EmptyState text="Бронирований пока нет" />}
                    {bookings.map((booking) => (
                        <div key={booking.id} style={styles.bookingCard}>
                          <div style={styles.itemTitle}>Бронь #{booking.id.slice(0, 8)}</div>
                          <div style={styles.bookingMetaBlock}>
                            <div>Стол: {booking.desk?.number ?? "—"}</div>
                            <div>Пространство: {booking.desk?.area?.name ?? "—"}</div>
                            <div>Пользователь: {booking.user?.id ?? "—"}</div>
                            <div>Дата: {formatDateTime(booking.date)}</div>
                          </div>
                          <button onClick={() => deleteBooking(booking.id)} style={styles.dangerButtonWide}>
                            Удалить бронирование
                          </button>
                        </div>
                    ))}
                  </div>
                </Card>
              </div>
          ) : (
              <div style={styles.gridBooking}>
                <Card title="Доступные столы">
                  <div style={styles.bookingFilters}>
                    <div style={styles.fieldBlock}>
                      <label style={styles.label}>Дата бронирования</label>
                      <input
                          type="date"
                          value={bookingDate}
                          onChange={(e) => setBookingDate(e.target.value)}
                          style={styles.input}
                      />
                      <p style={styles.helperText}>
                        Поле даты отображается в UI, но текущий backend его не принимает.
                      </p>
                    </div>

                    <div style={styles.fieldBlock}>
                      <label style={styles.label}>ID пользователя</label>
                      <input
                          value={userId}
                          onChange={(e) => setUserId(e.target.value)}
                          placeholder="UUID пользователя"
                          style={styles.input}
                      />
                    </div>
                  </div>

                  <div style={styles.deskGrid}>
                    {desks.length === 0 && <EmptyState text="Столы не найдены" />}
                    {desks.map((desk) => {
                      const isSelected = desk.id === selectedDeskId;
                      const hasBooking = bookings.some((booking) => booking.desk?.id === desk.id);

                      return (
                          <button
                              key={desk.id}
                              onClick={() => setSelectedDeskId(desk.id)}
                              style={{
                                ...styles.deskTile,
                                ...(isSelected ? styles.deskTileActive : {}),
                              }}
                          >
                            <div style={styles.deskTileHeader}>
                              <div style={styles.deskTileTitle}>Стол #{desk.number}</div>
                              <span style={hasBooking ? styles.statusBusy : styles.statusFree}>
                          {hasBooking ? "Занят" : "Свободен"}
                        </span>
                            </div>
                            <div style={styles.itemSecondary}>Пространство: {desk.area?.name ?? "—"}</div>
                            <div style={styles.itemMeta}>{desk.id}</div>
                          </button>
                      );
                    })}
                  </div>
                </Card>

                <Card title="Панель бронирования">
                  <div style={styles.formColumn}>
                    <div style={styles.selectedDeskCard}>
                      <div style={styles.label}>Выбранный стол</div>
                      <div style={styles.selectedDeskTitle}>
                        {selectedDesk ? `Стол #${selectedDesk.number}` : "Стол не выбран"}
                      </div>
                      <div style={styles.itemSecondary}>Пространство: {selectedDesk?.area?.name ?? "—"}</div>
                      <div style={styles.itemSecondary}>Дата: {bookingDate || "—"}</div>
                    </div>

                    <button onClick={createBooking} style={styles.bookButton}>
                      Забронировать
                    </button>

                    <div>
                      <h3 style={styles.sectionCaption}>Все бронирования</h3>
                      <div style={styles.stackList}>
                        {bookings.length === 0 && <EmptyState text="Бронирований пока нет" />}
                        {bookings.map((booking) => (
                            <div key={booking.id} style={styles.itemSoftCard}>
                              <div style={styles.itemTitle}>Стол #{booking.desk?.number ?? "—"}</div>
                              <div style={styles.itemSecondary}>Пространство: {booking.desk?.area?.name ?? "—"}</div>
                              <div style={styles.itemMeta}>Пользователь: {booking.user?.id ?? "—"}</div>
                              <div style={styles.itemMeta}>Дата: {formatDateTime(booking.date)}</div>
                            </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </Card>
              </div>
          )}
        </div>
      </div>
  );
}

function Card({ title, children }: { title: string; children: React.ReactNode }) {
  return (
      <section style={styles.card}>
        <h2 style={styles.cardTitle}>{title}</h2>
        {children}
      </section>
  );
}

function NavButton({
                     active,
                     onClick,
                     children,
                   }: {
  active: boolean;
  onClick: () => void;
  children: React.ReactNode;
}) {
  return (
      <button
          onClick={onClick}
          style={{
            ...styles.navButton,
            ...(active ? styles.navButtonActive : {}),
          }}
      >
        {children}
      </button>
  );
}

function EmptyState({ text }: { text: string }) {
  return <div style={styles.emptyState}>{text}</div>;
}

function formatDateTime(value?: string) {
  if (!value) {
    return "—";
  }

  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return value;
  }

  return date.toLocaleString("ru-RU");
}

function getErrorMessage(error: unknown) {
  if (error instanceof Error) {
    return error.message;
  }
  return "Произошла ошибка";
}

const styles: Record<string, React.CSSProperties> = {
  page: {
    minHeight: "100vh",
    background: `linear-gradient(180deg, ${PAGE_BACKGROUND} 0%, ${WHITE_COLOR} 100%)`,
    color: TEXT_PRIMARY,
    fontFamily:
        'Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif',
    padding: "32px 20px",
    boxSizing: "border-box",
  },
  shell: {
    maxWidth: "1320px",
    margin: "0 auto",
  },
  authShell: {
    maxWidth: "480px",
    margin: "80px auto 0",
  },
  authCard: {
    background: WHITE_COLOR,
    borderRadius: "28px",
    padding: "32px",
    border: `1px solid ${BORDER_COLOR}`,
    boxShadow: "0 16px 40px rgba(18, 51, 42, 0.06)",
  },
  headerCard: {
    background: `linear-gradient(135deg, ${WHITE_COLOR} 0%, #f7fffc 55%, ${LIGHT_GREEN} 100%)`,
    border: `1px solid ${BORDER_COLOR}`,
    borderRadius: "28px",
    padding: "28px 30px",
    boxShadow: "0 20px 60px rgba(0, 180, 130, 0.10)",
    marginBottom: "24px",
  },
  headerTopRow: {
    display: "flex",
    justifyContent: "space-between",
    gap: "20px",
    alignItems: "flex-start",
    flexWrap: "wrap",
  },
  badge: {
    display: "inline-flex",
    alignItems: "center",
    gap: "8px",
    borderRadius: "999px",
    background: LIGHT_GREEN,
    color: GREEN_COLOR,
    padding: "8px 12px",
    fontSize: "12px",
    fontWeight: 700,
    letterSpacing: "0.08em",
    marginBottom: "14px",
  },
  title: {
    margin: 0,
    fontSize: "38px",
    lineHeight: 1.05,
    fontWeight: 800,
  },
  subtitle: {
    margin: "10px 0 0",
    maxWidth: "760px",
    color: TEXT_SECONDARY,
    fontSize: "15px",
    lineHeight: 1.7,
  },
  navGroup: {
    display: "flex",
    gap: "10px",
    flexWrap: "wrap",
  },
  navButton: {
    border: `1px solid ${BORDER_COLOR}`,
    background: WHITE_COLOR,
    color: TEXT_PRIMARY,
    borderRadius: "18px",
    padding: "12px 18px",
    fontSize: "14px",
    fontWeight: 700,
    cursor: "pointer",
    transition: "all 0.2s ease",
    boxShadow: "0 8px 24px rgba(18, 51, 42, 0.05)",
  },
  navButtonActive: {
    background: GREEN_COLOR,
    color: WHITE_COLOR,
    borderColor: GREEN_COLOR,
    boxShadow: "0 14px 30px rgba(0, 180, 130, 0.25)",
  },
  actionsRow: {
    display: "flex",
    alignItems: "center",
    gap: "14px",
    marginBottom: "18px",
    flexWrap: "wrap",
  },
  loadingText: {
    color: TEXT_SECONDARY,
    fontSize: "14px",
  },
  primaryButton: {
    border: "none",
    background: GREEN_COLOR,
    color: WHITE_COLOR,
    borderRadius: "18px",
    padding: "12px 18px",
    fontSize: "14px",
    fontWeight: 700,
    cursor: "pointer",
    boxShadow: "0 14px 28px rgba(0, 180, 130, 0.22)",
  },
  bookButton: {
    border: "none",
    background: `linear-gradient(135deg, ${GREEN_COLOR} 0%, ${DARK_GREEN} 100%)`,
    color: WHITE_COLOR,
    borderRadius: "20px",
    padding: "15px 18px",
    fontSize: "16px",
    fontWeight: 800,
    cursor: "pointer",
    boxShadow: "0 18px 34px rgba(0, 180, 130, 0.25)",
  },
  dangerButton: {
    border: "none",
    background: DANGER_BG,
    color: DANGER_TEXT,
    borderRadius: "14px",
    padding: "10px 14px",
    fontSize: "13px",
    fontWeight: 700,
    cursor: "pointer",
    whiteSpace: "nowrap",
  },
  dangerButtonWide: {
    border: "none",
    background: DANGER_BG,
    color: DANGER_TEXT,
    borderRadius: "14px",
    padding: "11px 14px",
    fontSize: "13px",
    fontWeight: 700,
    cursor: "pointer",
    width: "100%",
    marginTop: "14px",
  },
  errorBanner: {
    marginBottom: "14px",
    padding: "14px 16px",
    borderRadius: "18px",
    background: DANGER_BG,
    color: DANGER_TEXT,
    border: "1px solid #ffdede",
  },
  successBanner: {
    marginBottom: "14px",
    padding: "14px 16px",
    borderRadius: "18px",
    background: LIGHT_GREEN,
    color: DARK_GREEN,
    border: `1px solid ${BORDER_COLOR}`,
  },
  card: {
    background: WHITE_COLOR,
    borderRadius: "28px",
    padding: "24px",
    border: `1px solid ${BORDER_COLOR}`,
    boxShadow: "0 16px 40px rgba(18, 51, 42, 0.06)",
    minWidth: 0,
  },
  cardTitle: {
    margin: "0 0 20px",
    fontSize: "22px",
    fontWeight: 800,
    letterSpacing: "-0.02em",
  },
  gridThree: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(320px, 1fr))",
    gap: "20px",
  },
  gridBooking: {
    display: "grid",
    gridTemplateColumns: "minmax(0, 1.3fr) minmax(320px, 0.9fr)",
    gap: "20px",
  },
  inlineForm: {
    display: "flex",
    gap: "10px",
    marginBottom: "18px",
    flexWrap: "wrap",
  },
  formColumn: {
    display: "flex",
    flexDirection: "column",
    gap: "14px",
  },
  input: {
    width: "100%",
    boxSizing: "border-box",
    borderRadius: "18px",
    border: `1px solid ${BORDER_COLOR}`,
    background: "#fbfffd",
    padding: "13px 15px",
    fontSize: "14px",
    color: TEXT_PRIMARY,
    outline: "none",
  },
  stackList: {
    display: "flex",
    flexDirection: "column",
    gap: "12px",
  },
  itemCard: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    gap: "12px",
    padding: "16px",
    borderRadius: "20px",
    border: `1px solid ${BORDER_COLOR}`,
    background: "#fcfffe",
  },
  itemSoftCard: {
    padding: "14px",
    borderRadius: "18px",
    border: `1px solid ${BORDER_COLOR}`,
    background: "#fcfffe",
  },
  bookingCard: {
    padding: "16px",
    borderRadius: "22px",
    border: `1px solid ${BORDER_COLOR}`,
    background: `linear-gradient(180deg, ${WHITE_COLOR} 0%, #fbfffd 100%)`,
  },
  itemTitle: {
    fontSize: "16px",
    fontWeight: 700,
    color: TEXT_PRIMARY,
  },
  itemSecondary: {
    marginTop: "5px",
    color: TEXT_SECONDARY,
    fontSize: "14px",
  },
  itemMeta: {
    marginTop: "6px",
    color: "#7d9d94",
    fontSize: "12px",
    wordBreak: "break-all",
  },
  bookingMetaBlock: {
    marginTop: "10px",
    display: "grid",
    gap: "6px",
    fontSize: "14px",
    color: TEXT_SECONDARY,
  },
  bookingFilters: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(240px, 1fr))",
    gap: "16px",
    marginBottom: "20px",
  },
  fieldBlock: {
    display: "flex",
    flexDirection: "column",
    gap: "8px",
  },
  label: {
    fontSize: "13px",
    fontWeight: 700,
    color: TEXT_SECONDARY,
  },
  helperText: {
    margin: 0,
    fontSize: "12px",
    lineHeight: 1.5,
    color: "#7e8e88",
  },
  deskGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
    gap: "14px",
  },
  deskTile: {
    textAlign: "left",
    borderRadius: "24px",
    border: `1px solid ${BORDER_COLOR}`,
    background: WHITE_COLOR,
    padding: "18px",
    cursor: "pointer",
    boxShadow: "0 12px 28px rgba(18, 51, 42, 0.04)",
  },
  deskTileActive: {
    border: `1px solid ${GREEN_COLOR}`,
    background: LIGHT_GREEN,
    boxShadow: "0 16px 30px rgba(0, 180, 130, 0.14)",
  },
  deskTileHeader: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    gap: "10px",
    marginBottom: "8px",
  },
  deskTileTitle: {
    fontSize: "18px",
    fontWeight: 800,
    color: TEXT_PRIMARY,
  },
  statusFree: {
    background: LIGHT_GREEN,
    color: DARK_GREEN,
    borderRadius: "999px",
    padding: "7px 10px",
    fontSize: "12px",
    fontWeight: 800,
    whiteSpace: "nowrap",
  },
  statusBusy: {
    background: DANGER_BG,
    color: DANGER_TEXT,
    borderRadius: "999px",
    padding: "7px 10px",
    fontSize: "12px",
    fontWeight: 800,
    whiteSpace: "nowrap",
  },
  selectedDeskCard: {
    padding: "18px",
    borderRadius: "22px",
    background: `linear-gradient(180deg, ${LIGHT_GREEN} 0%, ${WHITE_COLOR} 100%)`,
    border: `1px solid ${BORDER_COLOR}`,
  },
  selectedDeskTitle: {
    marginTop: "8px",
    fontSize: "24px",
    fontWeight: 800,
    color: TEXT_PRIMARY,
  },
  sectionCaption: {
    margin: "0 0 12px",
    fontSize: "12px",
    fontWeight: 800,
    color: TEXT_SECONDARY,
    textTransform: "uppercase",
    letterSpacing: "0.08em",
  },
  emptyState: {
    borderRadius: "18px",
    border: `1px dashed ${BORDER_COLOR}`,
    background: "#fbfffd",
    padding: "18px",
    color: TEXT_SECONDARY,
    fontSize: "14px",
  },
};
