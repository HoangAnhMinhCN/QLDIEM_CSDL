export default function Student() {
    const studentInfo = {
        name: "Nguyễn Văn A",
        grades: [
            { subject: "Toán", score: 8 },
            { subject: "Lý", score: 7 },
            { subject: "Hóa", score: 9 },
        ],
    };

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">🎓 Điểm của {studentInfo.name}</h1>
            <table className="w-full border">
                <thead className="bg-gray-200">
                <tr>
                    <th className="p-2 border">Môn học</th>
                    <th className="p-2 border">Điểm</th>
                </tr>
                </thead>
                <tbody>
                {studentInfo.grades.map((g, i) => (
                    <tr key={i}>
                        <td className="border p-2">{g.subject}</td>
                        <td className="border p-2 text-center">{g.score}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
