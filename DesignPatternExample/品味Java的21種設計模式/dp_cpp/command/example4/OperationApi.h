#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example4
				{
					///
					/// <summary> * ��������Ľӿ� </summary>
					/// 
					class OperationApi
					{
					///	
					///	 <summary> * ��ȡ������ɺ�Ľ�� </summary>
					///	 * <returns> ������ɺ�Ľ�� </returns>
					///	 
					public:
						virtual public int getResult() = 0;
					///	
					///	 <summary> * ���ü��㿪ʼ�ĳ�ʼֵ </summary>
					///	 * <param name="result"> ���㿪ʼ�ĳ�ʼֵ </param>
					///	 
						virtual public void setResult(int) = 0;
					///	
					///	 <summary> * ִ�мӷ� </summary>
					///	 * <param name="num"> ��Ҫ�ӵ��� </param>
					///	 
						virtual public void add(int) = 0;
					///	
					///	 <summary> * ִ�м��� </summary>
					///	 * <param name="num"> ��Ҫ������ </param>
					///	 
						virtual public void substract(int) = 0;
					};

				}
			}
		}
	}
}