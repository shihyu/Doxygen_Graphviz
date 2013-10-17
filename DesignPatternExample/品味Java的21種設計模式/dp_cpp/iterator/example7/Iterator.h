#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example7
				{
					///
					/// <summary> * �������ӿڣ�������ʺͱ���Ԫ�صĲ�����ʵ��˫����� </summary>
					/// 
					class Iterator
					{
					///	
					///	 <summary> * �ƶ����ۺ϶���ĵ�һ��λ�� </summary>
					///	 
					public:
						virtual public void first() = 0;
					///	
					///	 <summary> * �ƶ����ۺ϶������һ��λ�� </summary>
					///	 
						virtual public void next() = 0;
					///	
					///	 <summary> * �ж��Ƿ��Ѿ��ƶ��ۺ϶�������һ��λ�� </summary>
					///	 * <returns> true��ʾ�Ѿ��ƶ��ۺ϶�������һ��λ�ã�
					///	 *         false��ʾ��û���ƶ����ۺ϶�������һ��λ�� </returns>
					///	 
						virtual public bool isDone() = 0;
					///	
					///	 <summary> * ��ȡ�����ĵ�ǰԪ�� </summary>
					///	 * <returns> �����ĵ�ǰԪ�� </returns>
					///	 
						virtual public object *currentItem() = 0;


					///	
					///	 <summary> * �ж��Ƿ�Ϊ��һ��Ԫ�� </summary>
					///	 * <returns> ���Ϊ��һ��Ԫ�أ�����true�����򷵻�false </returns>
					///	 
						virtual public bool isFirst() = 0;
					///	
					///	 <summary> * �ƶ����ۺ϶������һ��λ�� </summary>
					///	 
						virtual public void previous() = 0;
					};

				}
			}
		}
	}
}