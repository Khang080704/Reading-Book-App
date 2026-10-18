UPDATE works
SET cover_id = CASE work_key
    WHEN 'OL82563W' THEN '15155833'
    WHEN 'OL82537W' THEN '15158664'
    WHEN 'OL82536W' THEN '10580435'
    WHEN 'OL82586W' THEN '15158660'
    WHEN 'OL82560W' THEN '12059372'
    WHEN 'OL82548W' THEN '15158666'
    WHEN 'OL82565W' THEN '10716273'
END
WHERE work_key IN (
    'OL82563W', 'OL82537W', 'OL82536W', 'OL82586W',
    'OL82560W', 'OL82548W', 'OL82565W'
);

UPDATE works
SET cover_id = CASE work_key
                   WHEN 'OL257943W' THEN '9269962'
                   WHEN 'OL257939W' THEN '8231751'
                   WHEN 'OL1955906W' THEN '11298743'
                   WHEN 'OL257948W' THEN '6501256'
                   WHEN 'OL257914W' THEN '15124196'
    END
WHERE work_key IN (
                   'OL257943W',
                   'OL257939W',
                   'OL1955906W',
                   'OL257948W',
                   'OL257914W'
    );