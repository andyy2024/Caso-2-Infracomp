import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import matplotlib.ticker as ticker

def cargar_datos_y_graficar(archivo, marco_objetivo, cmap_name=None, log_scale=False, box_out=False):
    # Cargar el archivo en un DataFrame
    df = pd.read_csv(archivo, sep=r'\s+')
    
    # Filtrar los datos por el marco seleccionado
    df_filtrado = df.copy()[df['marcos_asignados'] == marco_objetivo]
    
    # Calcular los tiempos
    df_filtrado['Tiempo de Hits'] = df_filtrado['numero_hits'] * 50e-6  # 50 ns a ms
    df_filtrado['Tiempo de Fallas'] = df_filtrado['numero_fallas'] * 1  # 1 ms por falla
    df_filtrado['Tiempo Total'] = df_filtrado['Tiempo de Hits'] + df_filtrado['Tiempo de Fallas']
    
    # Renombrar columnas
    df_filtrado = df_filtrado.rename(columns={'tamaño_pagina': 'Tamaño de Página'})
    
    # Graficar
    fig, ax = plt.subplots(figsize=(10,5))
    cmap = plt.get_cmap("RdPu" if cmap_name is None else cmap_name)
    df_filtrado.set_index('Tamaño de Página')[['Tiempo de Hits', 'Tiempo de Fallas', 'Tiempo Total']].plot(kind='bar', ax=ax, color=cmap(np.linspace(0,1,3)))
    
    ax.set_xlabel("Tamaño de Página (Bytes)")
    ax.set_ylabel('Tiempo (ms)\n(Nota: Escala Lineal)')
    ax.set_title(f'Tiempos para marcos asignados = {marco_objetivo}')
    plt.xticks(rotation=0)
    referencias = df["numero_fallas"][0] + df["numero_hits"][0]

    plt.axhline(y=referencias * 1, xmin=0, xmax=1, linestyle='-.', color='brown', label="Tiempo si todas fueran fallas")
    plt.axhline(y=referencias * 50e-6, xmin=0, xmax=1, linestyle='--', color="gray", label="Tiempo si todas fueran hits")
    
    if log_scale:
        ax.set_ylabel('Tiempo (ms)\n(Nota: Escala logarítmica)')
        ax.set_yscale('log')
        ax.yaxis.set_major_formatter(ticker.ScalarFormatter())

        # max_falla = df.groupby('marcos_asignados')['numero_fallas'].max()[marco_objetivo]
        max_falla = df['numero_fallas'].max()
        log = np.log10(max_falla)
        ceros = (int) (log//1)
        escala_logaritmica = [10**x for x in range(ceros+1)]
        ax.set_yticks(escala_logaritmica)

    plt.legend(title='Tipo de Tiempo')
    if box_out:
        plt.legend(title='Tipo de Tiempo',loc='center left', bbox_to_anchor=(1, 0.5))

    plt.tight_layout()
    plt.savefig("graphs/tiempos_marco_"+str(marco_objetivo)+".png", dpi=300)

archivo = 'output/data.txt'  # Cambia esto por la ruta de tu archivo
cargar_datos_y_graficar(archivo, marco_objetivo=1, cmap_name="cool", log_scale=True, box_out=True)
cargar_datos_y_graficar(archivo, marco_objetivo=2, cmap_name="winter", log_scale=True, box_out=True)
cargar_datos_y_graficar(archivo, marco_objetivo=3, cmap_name="autumn", log_scale = True, box_out=True)
cargar_datos_y_graficar(archivo, marco_objetivo=4, cmap_name="spring", log_scale=True)
cargar_datos_y_graficar(archivo, marco_objetivo=5, cmap_name="rainbow", log_scale=True)
cargar_datos_y_graficar(archivo, marco_objetivo=6, cmap_name="turbo", log_scale=True)
cargar_datos_y_graficar(archivo, marco_objetivo=7, cmap_name="gist_rainbow", log_scale=True)
cargar_datos_y_graficar(archivo, marco_objetivo=8, cmap_name="Wistia", log_scale=True)