-- auto-generated definition
create table p_hub
(
    hub_id     uuid not null
        primary key,
    created_at timestamp(6),
    deleted_at timestamp(6),
    updated_at timestamp(6),
    address    varchar(255),
    hub_name   varchar(255),
    latitude   numeric(10, 8),
    longitude  numeric(11, 8)
);

alter table p_hub
    owner to postgres;


INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('70a6354f-0a6e-4961-8b96-d06cee112770', '2025-11-05 20:28:43.366262', null, '2025-11-05 20:28:43.366262', '경기도 이천시 덕평로 257-21', '경기 남부 센터', 37.18962131, 127.37505001);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('d2519e13-175f-4bff-9a76-fdfdb6a2716a', '2025-11-05 19:35:50.143767', null, '2025-11-09 19:45:36.606676', '서울특별시 송파구 송파대로 55', '서울특별시 센터', 37.47000000, 127.12000000);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('047cd297-4e18-40c2-9b03-9f90e4bbbc85', '2025-11-05 20:14:55.027468', null, '2025-11-05 20:14:55.027468', '경기도 고양시 덕양구 권율대로 570', '경기 북부 센터', 37.64000000, 126.87000000);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('9cec6601-a3c3-4504-8206-4dbd207d5e9c', '2025-11-08 14:02:09.017614', null, '2025-11-08 14:02:09.017614', '울산 남구 중앙로 201', '울산광역시 센터', 35.53902710, 129.31135639);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('95c3710f-fd77-4d6e-bd13-4459db606e1c', '2025-11-09 19:39:56.191656', null, '2025-11-09 19:39:56.191656', '세종특별자치시 한누리대로 2130', '세종특별자치시 센터', 36.48005799, 127.28903941);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('b4868679-fc14-4710-b38f-6e5ce3a753b6', '2025-11-08 13:47:40.497389', null, '2025-11-08 13:47:40.497389', '광주 서구 내방로 111', '광주광역시 센터', 35.16009941, 126.85146193);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('4fcd188e-5985-46c2-b6bc-c9b901569e71', '2025-11-05 22:43:24.650462', null, '2025-11-05 22:43:24.650462', '부산 동구 중앙대로 206', '부산광역시 센터', 35.11760513, 129.04506022);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('1515e8bd-d16b-4704-9094-f525884afc6e', '2025-11-06 14:32:02.635022', null, '2025-11-06 14:32:02.635022', '대구 북구 태평로 161', '대구광역시 센터', 35.87588495, 128.59612921);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('a3185027-9035-499a-994b-1cbce11037a3', '2025-11-08 13:56:00.593381', null, '2025-11-08 13:57:19.495667', '대전 서구 둔산로 100', '대전광역시 센터', 36.35038500, 127.38463301);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('814639e5-6d0b-4451-8bf2-8426601a6713', '2025-11-09 19:43:44.730731', null, '2025-11-09 19:43:44.730731', '강원특별자치도 춘천시 중앙로 1', '강원특별자치도 센터', 37.88007292, 127.72790782);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('83b60d97-8944-40e4-8119-e5f7196c2e22', '2025-11-07 12:11:55.430151', null, '2025-11-09 23:16:54.789261', '인천 남동구 정각로 29', '인천광역시 센터', 37.45604996, 126.70525574);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('4ba23121-b3a9-4790-a893-f6fbda5bce99', '2025-11-10 12:08:23.843253', null, '2025-11-10 12:08:23.843253', '충북 청주시 상당구 상당로 82', '충청북도 센터', 36.63538679, 127.49142844);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('0dcb64dd-ffc7-405d-9586-cdfb3a65a483', '2025-11-10 12:09:00.554056', null, '2025-11-10 12:09:00.554056', '충남 홍성군 홍북읍 충남대로 21', '충청남도 센터', 36.65904170, 126.67305704);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('9b748ae8-b15d-4c44-8aa0-b9cca1d5bb69', '2025-11-10 12:09:51.421719', null, '2025-11-10 12:09:51.421719', '전북특별자치도 전주시 완산구 효자로 225', '전북특별자치도 센터', 35.81946217, 127.10639694);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('787aba46-c975-4c45-9a36-bee539a254ea', '2025-11-10 12:10:20.945094', null, '2025-11-10 12:10:20.945094', '전남 무안군 삼향읍 오룡길 1', '전라남도 센터', 34.81747277, 126.46541594);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('ef5e395c-6b41-4ee6-b07e-14ff055af631', '2025-11-10 12:12:37.579406', null, '2025-11-10 12:12:37.579406', '경남 창원시 의창구 중앙대로 300', '경상남도 센터', 35.23780325, 128.69194044);
INSERT INTO p_hub (hub_id, created_at, deleted_at, updated_at, address, hub_name, latitude, longitude) VALUES ('eae2a6c6-75e0-4c77-beef-cf83bc8fc2ec', '2025-11-10 12:11:33.224669', null, '2025-11-10 12:11:33.224669', '경북 안동시 풍천면 도청대로 455', '경상북도 센터', 36.57612055, 128.50572269);


-- auto-generated definition
create table p_hub_route
(
    route_id         uuid not null
        primary key,
    created_at       timestamp(6),
    deleted_at       timestamp(6),
    updated_at       timestamp(6),
    distance         bigint,
    is_active        boolean,
    time             bigint,
    arrival_hub_id   uuid not null
        constraint fks3qyil76udcyjualtvlhnb7f5
            references p_hub,
    departure_hub_id uuid not null
        constraint fko30lu9he43kxv0px4f46iwehf
            references p_hub,
    route_name varchar(255)
);

alter table p_hub_route
    owner to postgres;



INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('bff24ffb-e764-425a-bc5e-ec5c66209383', '2025-11-10 13:02:38.374829', null, '2025-11-10 14:21:44.234114', 81, true, 71, '70a6354f-0a6e-4961-8b96-d06cee112770', '83b60d97-8944-40e4-8119-e5f7196c2e22', '인천 -> 경기 남부');
INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('018f03a5-1bca-4c0b-a333-e03b42e1152e', '2025-11-10 12:19:51.284039', null, '2025-11-10 14:22:41.108899', 50, true, 49, '70a6354f-0a6e-4961-8b96-d06cee112770', 'd2519e13-175f-4bff-9a76-fdfdb6a2716a', '서울 -> 경기 남부');
INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('c0526e48-fe2f-4319-8dc0-e40f47780a7b', '2025-11-10 12:47:59.025769', null, '2025-11-10 14:23:08.248233', 242, true, 192, '1515e8bd-d16b-4704-9094-f525884afc6e', '70a6354f-0a6e-4961-8b96-d06cee112770', '경기 남부 -> 대구');
INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('74161f00-8019-43f5-938f-15dfa2da6de8', '2025-11-10 14:24:12.655088', null, '2025-11-10 14:24:12.655088', 81, true, 71, 'd2519e13-175f-4bff-9a76-fdfdb6a2716a', '70a6354f-0a6e-4961-8b96-d06cee112770', '경기 남부 -> 서울');
INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('69a615a5-89c9-4b2b-b9aa-77698495c5d6', '2025-11-10 14:28:05.556595', null, '2025-11-10 14:28:05.556595', 100, true, 50, 'd2519e13-175f-4bff-9a76-fdfdb6a2716a', '70a6354f-0a6e-4961-8b96-d06cee112770', '경기 남부 -> 서울 2');
INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('7f340927-b07f-4d9f-b0e1-6bf1e7789bf0', '2025-11-10 14:34:18.497594', '2025-11-10 14:34:50.164915', '2025-11-10 14:34:50.165821', 100, true, 50, '1515e8bd-d16b-4704-9094-f525884afc6e', '70a6354f-0a6e-4961-8b96-d06cee112770', '경기 남부 -> 서울 2');
INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('e35035b7-a641-4b6f-a279-48f715dc7095', '2025-11-10 14:35:23.381722', '2025-11-10 14:35:37.990783', '2025-11-10 14:35:37.991295', 100, true, 50, '83b60d97-8944-40e4-8119-e5f7196c2e22', '70a6354f-0a6e-4961-8b96-d06cee112770', '경기 남부 -> 서울 2');
INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('5e64a6e9-9f64-40d8-ae73-dc8a087caafd', '2025-11-10 12:49:36.437646', '2025-11-10 14:54:32.102461', '2025-11-10 14:54:32.124933', 242, true, 192, '4fcd188e-5985-46c2-b6bc-c9b901569e71', '1515e8bd-d16b-4704-9094-f525884afc6e', '대구 -> 부산');
INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('79c4222f-79ed-4e38-9537-4df429897a59', '2025-11-10 15:02:09.568263', null, '2025-11-10 15:02:09.568263', 242, true, 192, '4fcd188e-5985-46c2-b6bc-c9b901569e71', '1515e8bd-d16b-4704-9094-f525884afc6e', '대구 -> 부산');
INSERT INTO p_hub_route (route_id, created_at, deleted_at, updated_at, distance, is_active, time, arrival_hub_id, departure_hub_id, route_name) VALUES ('0c32f3e4-d3d5-4a83-90b6-b577ff3b2588', '2025-11-10 15:21:13.230718', null, '2025-11-10 15:21:13.230718', 200, true, 200, '4fcd188e-5985-46c2-b6bc-c9b901569e71', '1515e8bd-d16b-4704-9094-f525884afc6e', '대구 -> 부산 2');


