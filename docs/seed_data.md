Dựa vào cấu trúc và mối quan hệ bảng author_details, works, reading_resource, chapter. Hãy chuẩn bị seed data để insert và các bảng trên (vào file V2) theo yêu cầu sau (Điền đúng các thông tin yêu cầu, các thông tin còn bạn tự fill vào, đủ lớn và có ý nghĩa)
- Bảng author_details: insert 3 tác giả:
    + J. K. Rowling: ol_key = OL23919A, tên = J. K. Rowling
    + George R R Martin: ol_key = OL234664A, tên = George R. R. Martin
    + Christopher Tolkien: ol_key = OL26320A, tên = J.R.R. Tolkien
- Bảng work: insert các tác phẩm sau, tôi sẽ cung cấp title(work_key):
    + A Game of Thrones (OL257943W)
    + A class of Kings (OL257939W)
    + A storm of sword (OL257914W)
    + A feast for Crows (OL257948W)
    + A dance with Dragons (OL1955906W)
    + The Fellowship of the Ring (OL27513W)
    + The Two Towers (OL27479W)
    + The Return of the King (OL27455W)
    + Harry Potter and the Sorcerer's Stone (OL82563W)
    + Harry Potter and the Chamber of Secrets (OL82537W)
    + Harry Potter and the Prisoner of Azkaban (OL82536W)
    + Harry Potter and the Goblet of Fire (OL82560W)
    + Harry Potter and the Order of the Phoenix (OL82548W)
    + Harry Potter and the Half-Blood Prince (OL82565W)
    + Harry Potter and the Deathly Hallows (OL82586W)

- Bảng work_authors insert đủ id mối quan hệ sau khi 2 bảng trên đã insert

- Bảng chapter hãy dựa vào knownledge của bạn về các tác phẩm này, insert đầy đủ nhất có thể phần chapter và title của chapter
- Bảng reading resource map work_key với work_id của bảng work (lưu ý là id tự gen của bảng work, KHÔNG phải work_key), provider là INTERNAL, reading_mode là CHAPTER
    