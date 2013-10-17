#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example8
				{
					///
					/// <summary> * ���巭ҳ���ʾۺ�Ԫ�صĵ����ӿ� </summary>
					/// 
					class AggregationIterator
					{
					///	
					///	 <summary> * �ж��Ƿ�����һ��Ԫ�أ�����ν�Ƿ�һҳ�����ݣ�
					///	 * ��Ϊ�������ֻ��һ�����ݣ�Ҳ��Ҫ��һҳ�� </summary>
					///	 * <returns> �������һ��Ԫ�أ�����true��û����һ��Ԫ�ؾͷ���false </returns>
					///	 
					public:
						virtual public bool hasNext() = 0;
					///	
					///	 <summary> * ȡ�����漸��Ԫ�� </summary>
					///	 * <param name="num"> ��Ҫ��ȡ�ļ�¼���� </param>
					///	 * <returns> ���漸��Ԫ�� </returns>
					///	 
						virtual public Collection *next(int) = 0;
					///	
					///	 <summary> * �ж��Ƿ�����һ��Ԫ�أ�����ν�Ƿ�һҳ�����ݣ�
					///	 * ��Ϊ�������ֻ��һ�����ݣ�Ҳ��Ҫ��һҳ�� </summary>
					///	 * <returns> �������һ��Ԫ�أ�����true��û����һ��Ԫ�ؾͷ���false </returns>
					///	 
						virtual public bool hasPrevious() = 0;
					///	
					///	 <summary> * ȡ�����漸��Ԫ�� </summary>
					///	 * <param name="num"> ��Ҫ��ȡ�ļ�¼���� </param>
					///	 * <returns> ���漸��Ԫ�� </returns>
					///	 
						virtual public Collection *previous(int) = 0;
					};

				}
			}
		}
	}
}