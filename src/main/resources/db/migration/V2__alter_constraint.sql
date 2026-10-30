-- 1. Remove a restrição antiga
ALTER TABLE tb_user DROP CONSTRAINT fk_tb_user_sector;

-- 2. Cria a nova restrição permitindo o SET NULL
ALTER TABLE tb_user
    ADD CONSTRAINT fk_tb_user_sector
        FOREIGN KEY (sector_id)
            REFERENCES tb_sector(sector_id)
            ON DELETE SET NULL;
