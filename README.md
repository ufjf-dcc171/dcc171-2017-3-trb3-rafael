# Trabalho 3 de Laboratório de Programação 3

>Rafael Peixoto Santos de Lima
>
>Matrícula: 201276051
>
>Curso: Sistemas de Informação

## O sistema possui a seguinte lógica:
>1. um projeto pode ter nenhuma ou várias tarefas associadas.
>2. uma tarefa pode ter nenhuma ou várias pessoas associadas.
>3. cada projeto, ao ser cadastrado, é salvo no banco, na tabela projetos.
>4. cada tarefa, ao ser cadastrada, é salva no banco, na tabela tarefa com o nome do projeto referente.
>5. cada pessoa, ao ser cadastrada, é salva no banco, na tabela pessoa e na tabela projeto_tarefa_pessoa, com o nome do projeto e o nome da tarefa referente.
>6. o sistema pode cadastrar, alterar e remover pessoas, tarefas e projetos.
>7. ao remover projetos, suas tarefas e as pessoas associadas tambem sao removidas, exceto estejam presentes em outro projeto.
>8. ao remover tarefas, as pessoas associadas sao removidas, exceto estejam presentes em outras tarefas do mesmo ou de outros projetos.

## As melhorias que podem ser realizadas:
>1. aviso de erros.
>2. cadastro de tarefas e seleção das mesmas nos projetos.
>3. cadastro de pessoas e seleção das mesmas nas tarefas.
>4. criação de DAO para a tabela pessoa_tarefa_projeto.