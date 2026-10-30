-- 1. Remove a restrição antiga
ALTER TABLE tb_category DROP CONSTRAINT fk_tb_category_sector;

-- 2. Cria a nova restrição permitindo o SET NULL
ALTER TABLE tb_user
    ADD CONSTRAINT fk_tb_category_sector
        FOREIGN KEY (sector_id)
            REFERENCES tb_sector(sector_id)
            ON DELETE CASCADE;
