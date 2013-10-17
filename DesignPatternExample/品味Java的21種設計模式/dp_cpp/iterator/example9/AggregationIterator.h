#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example9
				{
					///
					/// <summary> * ���������ҳ���ʾۺ�Ԫ�صĵ����ӿ� </summary>
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
					///	 <summary> * �ж��Ƿ�����һ��Ԫ�أ�����ν�Ƿ�һҳ�����ݣ�
					///	 * ��Ϊ�������ֻ��һ�����ݣ�Ҳ��Ҫ��һҳ�� </summary>
					///	 * <returns> �������һ��Ԫ�أ�����true��û����һ��Ԫ�ؾͷ���false </returns>
					///	 
						virtual public bool hasPrevious() = 0;
					///	
					///	 <summary> * ȡ��ָ��ҳ�������� </summary>
					///	 * <param name="pageNum"> Ҫ��ȡ��ҳ�� </param>
					///	 * <param name="pageShow"> ÿҳ��ʾ���������� </param>
					///	 * <returns> ָ��ҳ�������� </returns>
					///	 
						virtual public Collection *getPage(int, int) = 0;
					};

				}
			}
		}
	}
}